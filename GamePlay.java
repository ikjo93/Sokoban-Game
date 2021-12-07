import java.util.*;

public class GamePlay {

    public void showMap(int[][] data){

        char elem; // 지도 데이터 요소

        StageMap stageMap = new StageMap();

        // 변환된 지도 데이터 값을 다시 원래대로 변환하여 콘솔창에 출력
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                elem = stageMap.getMapData(data[i][j]);
                // 구분선을 포함하고 있으면 출력하지 않음
                if (elem == '=') break;
                System.out.print(elem);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void moveMap(int[][] map_data, int[] player_pos){
        Scanner sc = new Scanner(System.in);
        char[] commands;
        while(true){
            System.out.print("w(위쪽), a(왼쪽), s(아래쪽), d(오른쪽), q(게임 종료) 중 하나 이상 입력해주세요 : ");
            commands = sc.nextLine().toCharArray();
            for (int i = 0; i < commands.length; i++) {
                if(commands[i] == 'q') {
                    System.out.println("게임을 종료합니다.");
                    return;
                }
                MapData mapData = checkCommand(map_data, player_pos, commands[i]);
                map_data = mapData.getMap_data();
                player_pos = mapData.getPlayer_pos();
                showMap(map_data);
            }
        }
    }

    public MapData checkCommand(int[][] data, int[] pos, char command){

        switch (command){
            case 'w':   if(data[pos[0]-1][pos[1]] == 32) {
                            data[pos[0]-1][pos[1]] = 3;
                            data[pos[0]][pos[1]] = 32;
                            pos[0]--;
                            System.out.printf("위로 이동합니다. 현재 위치 ↓ %n");
                        } else System.out.printf("(경고) 해당 명령을 수행할 수 없습니다!! 현재 위치 ↓ %n");
                        break;

            case 'a':   if(data[pos[0]][pos[1]-1] == 32) {
                            data[pos[0]][pos[1]-1] = 3;
                            data[pos[0]][pos[1]] = 32;
                            pos[1]--;
                            System.out.printf("왼쪽으로 이동합니다. 현재 위치 ↓ %n");
                        } else System.out.printf("(경고) 해당 명령을 수행할 수 없습니다!! 현재 위치 ↓ %n");
                        break;

            case 's':   if(data[pos[0]+1][pos[1]] == 32) {
                            data[pos[0]+1][pos[1]] = 3;
                            data[pos[0]][pos[1]] = 32;
                            pos[0]++;
                            System.out.printf("아래로 이동합니다. 현재 위치 ↓ %n");
                        } else System.out.printf("(경고) 해당 명령을 수행할 수 없습니다!! 현재 위치 ↓ %n");
                        break;

            case 'd':   if(data[pos[0]][pos[1]+1] == 32) {
                            data[pos[0]][pos[1]+1] = 3;
                            data[pos[0]][pos[1]] = 32;
                            pos[1]++;
                            System.out.printf("오른쪽으로 이동합니다. 현재 위치 ↓ %n");
                        } else System.out.printf("(경고) 해당 명령을 수행할 수 없습니다!! 현재 위치 ↓ %n");
                        break;

            default: System.out.printf("(경고) 잘못된 값을 입력했습니다!! 현재 위치 ↓ %n");

        }

        MapData mapData = new MapData();
        mapData.setMap_data(data);
        mapData.setPlayer_pos(pos);

        return mapData;
    }


}
