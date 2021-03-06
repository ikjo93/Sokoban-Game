import java.io.*;
import java.util.*;

public class StageMap {

    Map<Character, Integer> map_data_value;

    public StageMap() {
        // 지도 데이터별 변환값(공백 ' '는 9로 변환)
        map_data_value = new HashMap<>();
        map_data_value.put('#', 0);
        map_data_value.put('O', 1);
        map_data_value.put('o', 2);
        map_data_value.put('P', 3);
        map_data_value.put('=', 4);
        map_data_value.put('0', 5);
    }

    public List<MapData> createMap(int[][] map_size) throws Exception {

        AESCryptoUtil aesCryptoUtil = new AESCryptoUtil();

        String key = "Sokoban is good!";
        String specName = "AES/CBC/PKCS5Padding";
        String ivParameterSpec = "Sokoban is good!";

        File encryptedFile = new File("map_enc.txt");
        File decryptedFile = new File("map_dec.txt");
        aesCryptoUtil.decryptFile(specName, key, ivParameterSpec, encryptedFile, decryptedFile);

        BufferedReader reader = new BufferedReader(new FileReader("map_dec.txt"));

        // 스테이지별 지도 데이터(스테이지명, 지도 데이터)를 저장하기 위한 MapData 객체 선언
        MapData mapData;

        // MapData 객체 리스트 생성
        List<MapData> mapData_list = new ArrayList<>();

        // 구분선을 저장하기 위해 스테이지별 세로 길이 1씩 증가(마지막 스테이지 제외)
        for (int i = 0; i < map_size.length - 1; i++) {
            map_size[i][0]++;
        }

        // 지도 데이터 2차원 배열 선언
        int[][] map_data;

        // 스테이지별 지도 데이터 읽어오기
        String stage_name;
        int stage_level = 0;

        while((stage_name = reader.readLine()) != null) {

            // 지도 데이터를 저장하기 위한 MapData 객체 생성
            mapData = new MapData();

            // 스테이지명을 읽어오고 MapData 객체에 저장
            mapData.setStage_name(stage_name);

            // 스테이지별 가로, 세로 크기에 따라 지도 데이터 2차원 배열 생성
            map_data = new int[map_size[stage_level][0]][map_size[stage_level][1]];
            stage_level++;

            // 스테이지 데이터 2차원 배열을 9(' '를 의미) 값으로 초기화
            for (int[] ch : map_data) Arrays.fill(ch, 9);

            // 스테이지 데이터를 읽어오고 이때 앞서 생성한 Hashmap으로부터 스테이지 데이터별 변환값을 스테이지 데이터 2차원 배열에 각각 저장함
            for (int j = 0; j < map_data.length; j++) {

                char[] data = reader.readLine().toCharArray();

                for (int k = 0; k < data.length; k++) {
                    if (map_data_value.containsKey(data[k]))
                        map_data[j][k] = map_data_value.get(data[k]);
                }
            }

            // 읽어온 스테이지 데이터를 MapData 객체에 저장
            mapData.setMap_data(map_data);

            // MapData 객체를 MapData 객체 리스트에 추가
            mapData_list.add(mapData);
        }

        return mapData_list;
    }

    public void showMap(List<MapData> mapData_list){
        // 스테이지별 지도 데이터 출력하기
        for (MapData map : mapData_list) {
            // MapData 객체로부터 스테이지명을 불러와 출력
            System.out.println(map.getStage_name());
            System.out.println();

            // MapData 객체로부터 지도 데이터를 불러와 출력
            int[][] data = map.getMap_data();

            char elem; // 지도 데이터 요소
            // 구멍의 개수, 공의 개수, 플레이어 위치 x축(가로-열의 길이), 플레이어 위치 y축(세로-행의 길이)
            int hole_cnt = 0, ball_cnt = 0, player_posx = 0, player_posy = 0;
            boolean flag = false; // 구분선을 포함하고 있는지 여부

            // 변환된 지도 데이터 값을 다시 원래대로 변환하여 콘솔창에 출력
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    elem = getMapData(data[i][j]);
                    // 구분선을 포함하고 있으면 출력하지 않음
                    if (elem == '=') {
                        flag = true;
                        break;
                    }
                    System.out.print(elem);
                    if (elem == ' ') continue;
                    else if (elem == 'O') hole_cnt++;
                    else if (elem == 'o') ball_cnt++;
                    else if (elem == 'P') {
                        player_posx = j+1;
                        player_posy = i+1;
                    }
                }
                System.out.println();
            }

            // 구분선이 없는 마지막 스테이지의 경우 한줄 개행
            if(!flag) System.out.println();

            // 스테이지 관련 정보(지도 크기, 구멍과 공의 수 등) 출력
            System.out.printf("가로크기: %d%n", data[0].length);
            if(flag) // 구분 기호를 포함하는 경우
                System.out.printf("세로크기: %d%n", data.length-1);
            else // 구분 기호를 포함하지 않는 경우
                System.out.printf("세로크기: %d%n", data.length);
            System.out.printf("구멍의 수: %d%n", hole_cnt);
            System.out.printf("공의 수: %d%n", ball_cnt);
            System.out.printf("플레이어 위치 (%d, %d)%n%n", player_posy, player_posx);

            // 플레이어의 위치 저장
            int[] player_pos = {player_posy-1, player_posx-1};
            map.setPlayer_pos(player_pos);

            flag = false;
        }
    }

    // hashmap의 value 로 key 찾기
    public char getMapData(int value) {

        // value와 일치하는 key 반환
        for (Character key : map_data_value.keySet()) {
            if (value == map_data_value.get(key)) {
                return key;
            }
        }
        // value와 일치하는 key가 없는 경우 ' ' 반환
        return ' ';
    }

}
