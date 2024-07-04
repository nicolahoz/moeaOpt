package lahoz.moea.services;

import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lahoz.moea.global.MOEAProblem;
import lahoz.moea.infrastructure.dto.FrameDTO;
import lahoz.moea.infrastructure.dto.RequirementDTO;
import lahoz.moea.infrastructure.dto.StopDTO;
import lahoz.moea.infrastructure.dto.TrunkDTO;

@Service
public class MOEAService {
    private final FramesService frameService;
    private final StopService stopService;
    private final RequirementsService requirementService;
    private final TrunkService trunkService;

    private final HashMap<Long, StopDTO> stopMap = new HashMap<>();
    private Map<String, List<RequirementDTO>> requirementMap;
    private Map<String, List<TrunkDTO>> trunkMap;
    private final Map<Long, Map<Long, FrameDTO>> frameMap = new HashMap<>();
    private final String[] cargoTypes = { "PARTES", "SÓLIDO", "DELICADO", "LÍQUIDO", "INFLAMABLE" };

    public MOEAService(
            FramesService frameService,
            StopService stopService,
            RequirementsService requirementService,
            TrunkService trunkService
    ) {
        this.frameService = frameService;
        this.stopService = stopService;
        this.requirementService = requirementService;
        this.trunkService = trunkService;
    }

    public void initializeStops(){
        Page<StopDTO> stopPage = stopService.get("id", "DESC", 0, Integer.MAX_VALUE);

        for (StopDTO stop : stopPage.getContent()) {
            if(stop.getId() == 4533 || stop.getId() == 8652) continue;
            stopMap.put(stop.getId(), stop);
        }

        System.out.println("Total stops: " + stopMap.size());
    }

    public String getStopName(long id){
        return stopMap.get(id).getName();
    }

    public void initializeRequirements(){
        requirementMap = requirementService
                .get("id", "DESC", 0, Integer.MAX_VALUE)
                .stream()
                .filter(requirement ->
                        stopMap.containsKey(requirement.getId_stop_departure()) &&
                                stopMap.containsKey(requirement.getId_stop_arrival())
                )
                .collect(Collectors.groupingBy(RequirementDTO::getCategory));
    }

    public void initializeTrunks(){
        trunkMap = trunkService
                .get("id", "DESC", 0, Integer.MAX_VALUE)
                .stream()
                .filter(trunk -> stopMap.containsKey(trunk.getId_stop_parking()))
                .collect(Collectors.groupingBy(TrunkDTO::getCategory));
    }

    public void initializeFrames(){
        for(long stopId : stopMap.keySet()){
            frameMap.put(stopId, new HashMap<>());
            FrameDTO frame = new FrameDTO();
            frame.setIdStopDeparture(stopId);
            frame.setIdStopArrival(stopId);
            frame.setPrice(0.0);
            frame.setDeltaTime(0L);
            frameMap.get(stopId).put(stopId, frame);
        }

        List<FrameDTO> frames = frameService.get("id", "DESC", 0, Integer.MAX_VALUE).getContent();
        for (FrameDTO frame : frames) {
            if (!stopMap.containsKey(frame.getIdStopDeparture())) continue;
            if (!stopMap.containsKey(frame.getIdStopArrival())) continue;

            frameMap.get(frame.getIdStopDeparture()).put(frame.getIdStopArrival(), frame);
            frameMap.get(frame.getIdStopArrival()).put(frame.getIdStopDeparture(), frame);
        }

        ArrayList<long[]> missingFrames = new ArrayList<>();
        Long[] stopIds = stopMap.keySet().toArray(new Long[0]);
        for(int i = 0; i < stopIds.length - 1; i++){
            long stopIdA = stopIds[i];
            for(int j = i + 1; j < stopIds.length; j++){
                long stopIdB = stopIds[j];
                if(frameMap.get(stopIdA).get(stopIdB) == null) missingFrames.add(new long[]{stopIdA, stopIdB});
            }
        }
        System.out.print("Missing frames: " + missingFrames.size() + " => ");

        int prevCount = missingFrames.size();
        while(!missingFrames.isEmpty()){
            for(int i = 0; i < missingFrames.size(); i++){
                long[] pair = missingFrames.get(i);
                for(long otherStop : stopIds){
                    if(frameMap.get(pair[0]).get(otherStop) == null || frameMap.get(otherStop).get(pair[1]) == null) continue;
                    FrameDTO newFrame = new FrameDTO();
                    newFrame.setIdStopDeparture(frameMap.get(pair[0]).get(otherStop).getIdStopDeparture());
                    newFrame.setIdStopArrival(frameMap.get(otherStop).get(pair[1]).getIdStopArrival());
                    newFrame.setPrice(frameMap.get(pair[0]).get(otherStop).getPrice() + frameMap.get(otherStop).get(pair[1]).getPrice());
                    newFrame.setDeltaTime(frameMap.get(pair[0]).get(otherStop).getDeltaTime() + frameMap.get(otherStop).get(pair[1]).getDeltaTime());

                    frameMap.get(pair[0]).put(pair[1], newFrame);
                    frameMap.get(pair[1]).put(pair[0], newFrame);
                    missingFrames.remove(i);
                    break;
                }
            }
            if(prevCount == missingFrames.size()) break;
            prevCount = missingFrames.size();
        }

        System.out.println(missingFrames.size());
    }

    private Solution executeOptimization(String cargoType){
        MOEAProblem problem = new MOEAProblem(1, 3, 0, frameMap, requirementMap.get(cargoType), trunkMap.get(cargoType));

        Executor executor = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAIII")
                .withProperty("populationSize", 100)
                .withMaxTime(10000);

        NondominatedPopulation result = executor.run();

        return result.get(0);
    }

    public String runOptimization() throws InterruptedException {
        JSONObject resultData = new JSONObject();

        Arrays.stream(cargoTypes)
                .parallel()
                .forEach(cargoType -> {
                    float startTime = System.nanoTime();
                    Solution solution = this.executeOptimization(cargoType);

                    JSONObject solutionJSON = new JSONObject();
                    solutionJSON.put("computingTime", (System.nanoTime() - startTime) / 1000000000);
                    solutionJSON.put("objectives", new JSONArray(solution.getObjectives()));
                    solutionJSON.put("variable0", new JSONArray(solution.getVariable(0).toString()));
                    JSONArray trunkDetails = new JSONArray();

                    double[] times = (double[]) solution.getAttribute("times");
                    int[] capacities = (int[]) solution.getAttribute("capacities");
                    List<Integer>[] requirementsPerTrunk = (List<Integer>[]) solution.getAttribute("requirementsPerTrunk");

                    for(int i = 0; i < trunkMap.get(cargoType).size(); i++){
                        JSONObject trunkDetail = new JSONObject();
                        trunkDetail.put("id", trunkMap.get(cargoType).get(i).getId());
                        trunkDetail.put("capacity", trunkMap.get(cargoType).get(i).getCapacity());
                        trunkDetail.put("requirements", new JSONArray(requirementsPerTrunk[i]));
                        trunkDetail.put("time", times[i]);
                        trunkDetail.put("capacity", capacities[i]);

                        long currentStop = trunkMap.get(cargoType).get(i).getId_stop_parking();
                        String path = "Route: " + getStopName(currentStop) + "\n";

                        for(int requirementIndex : requirementsPerTrunk[i]){
                            RequirementDTO requirement = requirementMap.get(cargoType).get(requirementIndex);
                            path += "Req No: " + requirement.getId() + " => " + getStopName(requirement.getId_stop_departure()) + " => " + getStopName(requirement.getId_stop_arrival()) + "\n";
                        }

                        trunkDetail.put("path", path);

                        trunkDetails.put(trunkDetail);
                    }

                    solutionJSON.put("trunkDetails", trunkDetails);

                    resultData.put(cargoType, solutionJSON);
                });

        return resultData.toString();
    }
}