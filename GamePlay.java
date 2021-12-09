import java.io.*;
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

    public boolean moveMap(MapData mapData) throws IOException {
        Scanner sc = new Scanner(System.in);

        int[][] map_data = mapData.getMap_data();
        int row = map_data.length, col = map_data[0].length, turn = 0, slot_num = 0;
        int[][] map_data_origin = new int[row][col];
        for (int i = 0; i < row; i++) for (int j = 0; j < col; j++) map_data_origin[i][j] = map_data[i][j];

        int x = mapData.getPlayer_pos()[0], y = mapData.getPlayer_pos()[1];
        int[] player_pos = {x, y}, player_pos_origin = {x, y};

        char[] commands;
        char command;
        String stage_name = mapData.getStage_name();

        // 지도 데이터와 플레이어의 위치 로그 저장 리스트 자료구조
        List<int[][]> map_log = new ArrayList<>();
        List<int[]> player_log = new ArrayList<>();
        map_log.add(map_data_origin);
        player_log.add(player_pos_origin);
        int[][] map_data_log;
        int[] player_pos_log;
        int index = 0;

        while(true) {

            System.out.printf("※ 부가 기능 : q(게임 종료), r(스테이지 초기화), u(한 턴 되돌리기), U(턴 되돌리기 취소), 1~5(세이브 슬롯 선택), S(세이브), L(불러오기)%n명령어 w(위쪽), a(왼쪽), s(아래쪽), d(오른쪽) 중 하나 이상 입력해주세요. : ");
            commands = sc.nextLine().toCharArray();

            for (int i = 0; i < commands.length; i++) {
                command = commands[i];
                if(command == 'q') return true;
                else if(command == 'r') {
                    initStage(map_data, map_data_origin, map_log, player_pos, player_pos_origin, player_log);
                    turn = 0;
                    continue;
                } else if( '1' <= command && command <= '5' ) {
                    slot_num = command - 48;
                    System.out.printf("%d번 세이브 슬롯 선택%n%n", slot_num);
                    continue;
                } else if(command == 'S'){
                    if(slot_num == 0) System.out.printf("세이브 슬롯이 선택되지 않았습니다!!%n%n");
                    else slot_num = saveMap(slot_num, mapData, map_data_origin, player_pos_origin);
                    continue;
                } else if (command == 'L') {
                    if(slot_num == 0) System.out.printf("세이브 슬롯이 선택되지 않았습니다!!%n%n");
                    else {
                        mapData = loadMap(slot_num, map_log, player_log);
                        stage_name = mapData.getStage_name();
                        map_data = mapData.getMap_data();
                        player_pos = mapData.getPlayer_pos();
                        map_data_origin = mapData.getMap_data_origin();
                        player_pos_origin = mapData.getPlayer_pos_origin();
                        turn = 0;
                    }
                    slot_num = 0;
                    continue;
                } else if(command == 'u' || command == 'U') {
                    turn = goBackOrForward(command, turn, map_data, map_log, player_pos, player_log);
                    continue;
                }

                mapData = checkCommand(command, stage_name, map_data, map_data_origin, player_pos);
                if(mapData == null) continue;
                else {
                    showMap(map_data);
                    int loop = map_log.size() - 1;
                    while (loop > turn) {
                        map_log.remove(loop);
                        player_log.remove(loop);
                        loop--;
                    }
                }

                if(checkClear(map_data, map_data_origin) == true) {
                    System.out.printf("%s 클리어!! 총 %d 턴수가 소요되었습니다!!%n%n", stage_name, turn+1);
                    return false;
                }

                // 변경된 지도 데이터와 플레이어의 위치 로깅 작업
                row = map_data.length;
                col = map_data[0].length;
                map_data_log = new int[row][col];
                for (int j = 0; j < row; j++) for (int k = 0; k < col; k++) map_data_log[j][k] = map_data[j][k];
                player_pos_log = new int[]{player_pos[0], player_pos[1]};
                map_log.add(map_data_log);
                player_log.add(player_pos_log);
                turn++;
                System.out.printf("현재 턴수 : %d%n", turn);
            }
        }
    }

    public void initStage(int[][] map_data, int[][] map_data_origin, List<int[][]> map_log, int[] player_pos, int[] player_pos_origin, List<int[]> player_log){
        int row = map_data.length, col = map_data[0].length;

        for (int j = 0; j < row; j++) for (int k = 0; k < col; k++) map_data[j][k] = map_data_origin[j][k];
        player_pos[0] = player_pos_origin[0];
        player_pos[1] = player_pos_origin[1];
        System.out.println("스테이지를 초기화 합니다.");

        map_log.clear();
        map_log.add(map_data_origin);
        player_log.clear();
        player_log.add(player_pos_origin);

        System.out.printf("현재 턴수 : 0%n%n");

        showMap(map_data);
    }

    public int saveMap(int slot_num, MapData mapData, int[][] map_data_origin, int[] player_pos_origin) throws IOException {

        File file = new File("C:\\save\\slot " + slot_num + ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter((file)));
        StringBuilder sb = new StringBuilder();

        int row = mapData.getMap_data().length, col = mapData.getMap_data()[0].length;

        writer.write(mapData.getStage_name() + "\n");
        writer.write(mapData.getPlayer_pos()[0] + "\n" + mapData.getPlayer_pos()[1] + "\n");
        writer.write(row + "\n" + col + "\n");

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                sb.append(mapData.getMap_data()[i][j]);
            }
            sb.append("\n");
            writer.write(sb.toString());
            sb.delete(0, sb.length());
        }

        writer.write(player_pos_origin[0] + "\n" + player_pos_origin[1] + "\n");

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                sb.append(map_data_origin[i][j]);
            }
            sb.append("\n");
            writer.write(sb.toString());
            sb.delete(0, sb.length());
        }

        writer.close();
        System.out.printf("%d번 세이브 슬롯에 현재 데이터를 저장했습니다.%n", slot_num);
        return 0;
    }

    public MapData loadMap(int slot_num, List<int[][]> map_log, List<int[]> player_log) throws IOException {

        MapData mapData = new MapData();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\save\\slot " + slot_num + ".txt"));

        mapData.setStage_name(reader.readLine());
        mapData.setPlayer_pos(new int[]{Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine())});

        int row = Integer.parseInt(reader.readLine()), col = Integer.parseInt(reader.readLine());

        int[][] map_data = new int[row][col];
        int[][] map_data_log = new int[row][col];
        char[] data;
        for (int i = 0; i < row; i++) {
            data = reader.readLine().toCharArray();
            for (int j = 0; j < col; j++) {
                map_data[i][j] = data[j]-48;
                map_data_log[i][j] = data[j]-48;
            }
        }
        mapData.setMap_data(map_data);

        mapData.setPlayer_pos_origin(new int[]{Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine())});
        map_data = new int[row][col];
        for (int i = 0; i < row; i++) {
            data = reader.readLine().toCharArray();
            for (int j = 0; j < col; j++) {
                map_data[i][j] = data[j]-48;
            }
        }
        mapData.setMap_data_origin(map_data);

        map_log.clear();
        map_log.add(map_data_log);
        player_log.clear();
        player_log.add(new int[]{mapData.getPlayer_pos()[0], mapData.getPlayer_pos()[1]});

        System.out.printf("%s%n", mapData.getStage_name());
        showMap(mapData.getMap_data());
        System.out.printf("%d번 세이브 슬롯으로부터 데이터를 불러왔습니다. 현재 턴수 : 0%n", slot_num);

        return mapData;
    }

    public int goBackOrForward(char command, int turn, int[][] map_data, List<int[][]> map_log, int[] player_pos, List<int[]> player_log){
        int row = map_data.length, col = map_data[0].length;

        if(command == 'u'){
            if(turn < 1) {
                System.out.printf("(경고) 턴을 되돌릴 수 없습니다.%n%n");
                return turn;
            }
            turn--;
            System.out.printf("한 턴을 되돌립니다. 현재 턴 수 : %d%n", turn);
        } else {
            if(turn > map_log.size() - 2) {
                System.out.printf("(경고) 턴 되돌리기를 취소할 수 없습니다.%n%n");
                return turn;
            }
            turn++;
            System.out.printf("한 턴 되돌리기를 취소합니다. 현재 턴 수 : %d%n", turn);
        }

        for (int j = 0; j < row; j++) for (int k = 0; k < col; k++) map_data[j][k] = map_log.get(turn)[j][k];
        player_pos[0] = player_log.get(turn)[0];
        player_pos[1] = player_log.get(turn)[1];

        showMap(map_data);

        return turn;
    }

    public MapData checkCommand(char command, String stage_name, int[][] data, int[][] origin_data, int[] pos){

        MapData mapData = new MapData();

        int x = 0, y = 0, row = pos[0], col = pos[1]; // 이동할 좌표(x, y)와 현재 플레이어의 위치(row, col)
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
        if((frontObj == 2 || frontObj == 5) && (moreFrontObj == 9 || moreFrontObj == 1)) {
            if (moreFrontObj == 1) data[row+x_][col+y_] = 5; // 구멍이 공에 가려진 경우
            else data[row+x_][col+y_] = 2;
        } else if(frontObj == 0 || ((frontObj == 2 || frontObj == 5) && (moreFrontObj != 1 || moreFrontObj != 9))) {
            System.out.printf("(경고) 해당 명령을 수행할 수 없습니다!!%n");
            return null;
        }

        // 기존 플레이어의 위치의 요소 수정
        if(origin_data[row][col] == 1 || origin_data[row][col] == 5) data[row][col] = 1;
        else data[row][col] = 9;

        // 플레이어의 새로운 위치
        data[row+x][col+y] = 3;
        pos[0] += x;
        pos[1] += y;
        if( x == -1 ) System.out.printf("위로 이동합니다. 현재 위치 ↓ %n");
        else if( y == -1 ) System.out.printf("왼쪽로 이동합니다. 현재 위치 ↓ %n");
        else if( x == 1 ) System.out.printf("아래로 이동합니다. 현재 위치 ↓ %n");
        else System.out.printf("오른쪽으로 이동합니다. 현재 위치 ↓ %n");

        mapData.setStage_name(stage_name);
        mapData.setMap_data(data);
        mapData.setPlayer_pos(pos);

        return mapData;
    }

    public boolean checkClear(int[][] data, int[][] map_data_origin){
        for (int i = 0; i < data.length ; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if(data[i][j] == 1 || (data[i][j] == 3 && (map_data_origin[i][j] == 1 || map_data_origin[i][j] == 5))) return false;
                if(data[i][j] == 4) break; // 구분선인 경우 for문 break
            }
        }
        return true;
    }

}