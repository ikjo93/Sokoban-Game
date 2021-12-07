import java.util.*;

public class GamePlay {

    public void showMap(int[][] data) {

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

    public boolean moveMap(MapData mapData) {
        Scanner sc = new Scanner(System.in);

        int[][] map_data = mapData.getMap_data();
        int row = map_data.length, col = map_data[0].length;
        int[][] map_data_origin = new int[row][col];
        for (int i = 0; i < row; i++) for (int j = 0; j < col; j++) map_data_origin[i][j] = map_data[i][j];

        int x = mapData.getPlayer_pos()[0], y = mapData.getPlayer_pos()[1];
        int[] player_pos = {x, y};
        int[] player_pos_origin = {x, y};

        int turn = 0;
        char[] commands;
        boolean clear_flag = false;
        String stage_name = mapData.getStage_name();

        while(true) {
            System.out.print("w(위쪽), a(왼쪽), s(아래쪽), d(오른쪽), q(게임 종료), r(스테이지 초기화) 중 하나 이상 입력해주세요 : ");
            commands = sc.nextLine().toCharArray();
            for (int i = 0; i < commands.length; i++) {
                if(commands[i] == 'q') {
                    System.out.println("게임을 종료합니다.");
                    return true;
                } else if(commands[i] == 'r') {
                    for (int j = 0; j < row; j++) for (int k = 0; k < col; k++) map_data[j][k] = map_data_origin[j][k];
                    player_pos[0] = player_pos_origin[0];
                    player_pos[1] = player_pos_origin[1];
                    System.out.println("스테이지를 초기화 합니다.");
                    turn = 0;
                    showMap(map_data);
                    continue;
                }

                mapData = checkCommand(map_data, map_data_origin, player_pos, commands[i]);
                if(!(mapData == null)) turn++;
                else continue;
                System.out.printf("현재 턴수 : %d%n%n", turn);

                map_data = mapData.getMap_data();
                player_pos = mapData.getPlayer_pos();
                showMap(map_data);

                if(clear_flag = checkClear(map_data, map_data_origin) == true) {
                    System.out.printf("%s 클리어!! 총 %d 턴수가 소요되었습니다!!%n%n", stage_name, turn);
                    return false;
                }
            }
        }
    }

    public MapData checkCommand(int[][] data, int[][] origin_data, int[] pos, char command){

        MapData mapData = new MapData();
        // 이동할 좌표(x, y)와 현재 플레이어의 위치(row, col)
        int x = 0, y = 0, row = pos[0], col = pos[1];
        if (command == 'w') x--;
        else if (command == 'a') y--;
        else if (command == 's') x++;
        else if (command == 'd') y++;
        else {
            System.out.printf("(경고) 잘못된 값을 입력했습니다!!%n");
            return null;
        }

        int x_ = x*2, y_ = y*2;
        int frontObj = data[row+x][col+y], moreFrontObj = 0;
        if( row+x_ < data.length && col+y_ < data[0].length && row+x_ > -1 && col+y_ > -1) moreFrontObj = data[row+x_][col+y_];

        // 플레이어의 이동조건
        if((frontObj == 2 || frontObj == 5) && (moreFrontObj == 32 || moreFrontObj == 1)) {
            if (moreFrontObj == 1) data[row+x_][col+y_] = 5; // 구멍이 공에 가려진 경우
            else data[row+x_][col+y_] = 2;
        } else if(frontObj == 0 || ((frontObj == 2 || frontObj == 5) && (moreFrontObj != 1 || moreFrontObj != 32))) {
            System.out.printf("(경고) 해당 명령을 수행할 수 없습니다!!%n");
            return null;
        }

        // 기존 플레이어의 위치의 요소 수정
        if(origin_data[row][col] == 1 || origin_data[row][col] == 5) data[row][col] = 1;
        else data[row][col] = 32;

        // 플레이어의 새로운 위치
        data[row+x][col+y] = 3;
        pos[0] += x;
        pos[1] += y;
        if( x == -1 ) System.out.printf("위로 이동합니다. 현재 위치 ↓ %n");
        else if( y == -1 ) System.out.printf("왼쪽로 이동합니다. 현재 위치 ↓ %n");
        else if( x == 1 ) System.out.printf("아래로 이동합니다. 현재 위치 ↓ %n");
        else System.out.printf("오른쪽으로 이동합니다. 현재 위치 ↓ %n");

        mapData.setMap_data(data);
        mapData.setPlayer_pos(pos);

        return mapData;
    }

    public boolean checkClear(int[][] data, int[][] map_data_origin){
        for (int i = 0; i < data.length ; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if(data[i][j] == 1 || (data[i][j] == 3 && (map_data_origin[i][j] == 1 || map_data_origin[i][j] == 5))) return false;
                if(data[i][j] == 4) break; // 구분선인 경우 for문 탈출
            }
        }
        return true;
    }

}