public class MapData {

    private String stage_name;
    private int[][] map_data;
    private int[] player_pos;
    private int[][] map_data_origin;
    private int[] player_pos_origin;

    public String getStage_name() {
        return stage_name;
    }

    public int[][] getMap_data() {
        return map_data;
    }

    public int[] getPlayer_pos() { return player_pos; }

    public int[][] getMap_data_origin() { return map_data_origin; }

    public int[] getPlayer_pos_origin() { return player_pos_origin; }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public void setMap_data(int[][] map_data) {
        this.map_data = map_data;
    }

    public void setPlayer_pos(int[] player_pos) { this.player_pos = player_pos; }

    public void setMap_data_origin(int[][] map_data_origin) { this.map_data_origin = map_data_origin; }

    public void setPlayer_pos_origin(int[] player_pos_origin) { this.player_pos_origin = player_pos_origin; }
}
