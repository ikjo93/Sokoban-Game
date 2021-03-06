import java.io.IOException;
import java.util.*;

public class Sokoban {

    public static void main(String[] args) throws Exception {

        // 1단계 : 지도 데이터 출력하기
        // 스테이지별 지도의 가로, 세로 길이 크기 설정
        int[][] map_size = {{3, 5}, {7, 11}, {6, 6}, {6, 7}, {7, 7}};

        StageMap stageMap = new StageMap();
        List<MapData> mapData_list = stageMap.createMap(map_size);
        stageMap.showMap(mapData_list);

        /* 2단계 : 플레이어 이동 구현하기
        int stage_level = 2;
        System.out.println("Game Start!!");
        System.out.printf("Stage %d%n", stage_level);
        System.out.println();

        MapData mapData = mapData_list.get(stage_level-1);
        GamePlay gamePlay = new GamePlay();

        gamePlay.showMap(mapData.getMap_data());
        gamePlay.moveMap(mapData.getMap_data(), mapData.getPlayer_pos());*/

        // 3단계: 소코반 게임 완성하기 + 4단계 추가기능 구현
        MapData mapData;
        GamePlay gamePlay;
        boolean quit_flag = false;
        int last_level = mapData_list.size();

        for (int i = 1; i <= last_level; i++) {
            System.out.println("Game Start!!");
            System.out.printf("Stage %d%n", i);
            System.out.println();

            mapData = mapData_list.get(i-1);
            gamePlay = new GamePlay();

            gamePlay.showMap(mapData.getMap_data());
            quit_flag = gamePlay.moveMap(mapData);
            if(quit_flag) {
                System.out.println("게임을 종료합니다.");
                break;
            }
        }

        if(!quit_flag) System.out.println("축하합니다!! 모든 스테이지를 클리어 했습니다!!");

    }

}
