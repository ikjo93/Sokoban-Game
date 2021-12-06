import java.util.*;

public class Sokoban {

    public static void main(String[] args) {

        // 1단계 : 지도 데이터 출력하기 
        // 스테이지별 지도의 가로, 길이 크기 설정
        int[][] map_size = {{3, 5}, {7, 11}};

        StageMap stageMap = new StageMap();
        List<MapData> mapData_list = stageMap.createMap(map_size);
        stageMap.showMap(mapData_list);

    }

}