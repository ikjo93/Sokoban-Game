public class MapData {

    private String stage_name;
    private int[][] map_data;
    private int[] player_pos;

    public String getStage_name() {
        return stage_name;
    }

    public int[][] getMap_data() {
        return map_data;
    }

    public int[] getPlayer_pos() { return player_pos; }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public void setMap_data(int[][] map_data) {
        this.map_data = map_data;
    }

    public void setPlayer_pos(int[] player_pos) { this.player_pos = player_pos; }
}
