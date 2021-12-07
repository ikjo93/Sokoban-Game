import java.util.*;

public class Sokoban {

    public static void main(String[] args) {

        // 1단계 : 지도 데이터 출력하기
        // 스테이지별 지도의 가로, 세로 길이 크기 설정
        int[][] map_size = {{3, 5}, {7, 11}};

        StageMap stageMap = new StageMap();
        List<MapData> mapData_list = stageMap.createMap(map_size);
        stageMap.showMap(mapData_list);

        // 2단계 : 플레이어 이동 구현하기
        int stage_level = 2;
        System.out.println("Game Start!!");
        System.out.printf("Stage %d%n", stage_level);
        System.out.println();

        MapData mapData = mapData_list.get(stage_level-1);
        GamePlay gamePlay = new GamePlay();

        gamePlay.showMap(mapData.getMap_data());
        gamePlay.moveMap(mapData.getMap_data(), mapData.getPlayer_pos());


    }

}