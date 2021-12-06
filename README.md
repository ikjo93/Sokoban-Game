# 구현과정 상세 설명

## 1단계
> ### 풀이 과정
>	> + #### 전체 클래스 구성
>	>
>	> >  + Sokoban 클래스(main 클래스)
>	> >  + StageMap 클래스(지도 데이터 읽기, 출력)
>	> >  + MapData 클래스(스테이지별 지도 데이터 저장 및 전송)
>	>
>	> + #### 풀이 과정
>	> >  + 하나의 소스 파일에 절차식으로 코드를 작성하기 보다 기능별로 분리하여 코드를 작성하고자 했습니다.
>	> >    + 기능별로 main 클래스 역할을 하는 Sokoban 클래스, 지도 데이터를 읽어오고 출력하는 StageMap 클래스, 콘솔창으로부터 지도 데이터를 입력받을 때와 콘솔창에 지도 데이터를 출력시킬 때를 위해 데이터를 저장하고 전송하는 용도의 MapData 클래스로 나누어 코드를 작성했습니다.
>	>	>
>	> >  + main 메서드를 포함하고 있는 Sokoban 클래스에서 StageMap 객체를 생성하여 createMap 메서드를 통해 콘솔창으로부터 지도 데이터 리스트를 입력받도록 했고, 이를 파라미터로 두어 showMap 메서드를 통해 콘솔창에 지도 데이터를 출력시키도록 했습니다.
>	> >    + 이때 createMap 메서드가 실행될 때는 MapData 객체를 생성하여 지도 데이터를 입력받도록 했습니다. 마찬가지로 showMap 메서드 실행 시(출력 시)에는 데이터가 저장된 MapData 객체로부터 데이터를 꺼내와 데이터를 콘솔창에 출력하도록 했습니다.
>	>	>
>	> >  + 클래스 다이어그램
>	> > ![캡처](https://user-images.githubusercontent.com/82401504/144848276-0bd13d17-a041-4bba-9fb5-5fda0aa6d1dd.PNG)





<br/>
<hr/>
<br/>

> ### 코드 설명
>	> + #### Sokoban 클래스(main 클래스)
>	> >  + Sokoban 클래스는 해당 프로그램의 메인 클래스로서 main 메서드를 포함하고 있습니다.
>	> >  + 스테이지별로 지도의 가로 길이와 세로 길이를 2차원 배열(map_size)에 저장하고 있습니다.
>	> >    + 해당 코드에서는 2개의 스테이지에 대해 크기를 설정했습니다.
> > >  ```Java
> > >  int[][] map_size = {{3, 5}, {7, 11}};
> > >  ```
>	> >  + StageMap 객체를 생성한 후 2차원 배열(map_size)을 인자로 두어 createMap 메서드를 호출하여 스테이지별로 지도 데이터를 읽어오고, 리스트(mapData_list)에 저장합니다. 
>	> >  + 이후 해당 리스트(mapData_list)를 인자로 두어 showMap 메서드를 호출하여 스테이지별로 지도 데이터를 출력합니다.
> > >  ```Java
> > >  StageMap stageMap = new StageMap();
> > >  List<MapData> mapData_list = stageMap.createMap(map_size);
> > >  stageMap.showMap(mapData_list);
> > >  ```
>	>
>	> + #### StageMap 클래스(지도 데이터 읽기, 출력)
>	> >  + StageMap 클래스는 hashMap 필드와 StageMap 생성자 그리고 createMap 메서드, showMap 메서드, getMapData 메서드로 구성되어있습니다.
>	> >  + 우선 hashMap 필드는 지도 데이터(#, O, o 등)별로 변환값(0 ~ 4)을 저장하는 역할을 하며, StageMap 생성자 호출 시(StageMap 객체 생성 시) 각각의 데이터별로 변환시킬 값을 put 해주었습니다.
>	> >    + 이는 콘솔창으로부터 데이터를 입력받을 때 사용되고(#, O, o 등 -> 0 ~4) 이후 가공, 처리된 데이터를 콘솔창에 출력시킬 때 사용됩니다.(0 ~4 -> #, O, o 등)
> > >  ```Java
> > >  Map<Character, Integer> map_data_value;
> > >  
> > >  public StageMap() {
> > >   // 지도 데이터별 변환값
> > >   this.map_data_value = new HashMap<>();
> > >   map_data_value.put('#', 0);
> > >   map_data_value.put('O', 1);
> > >   map_data_value.put('o', 2);
> > >   map_data_value.put('P', 3);
> > >   map_data_value.put('=', 4);
> > >  }
> > >  ```
>	> >  + createMap 메서드는 Scanner 객체를 통해 콘솔창으로부터 지도 데이터를 입력받아 스테이지별 지도 데이터를 저장하는 역할을 합니다.
>	> >    + 이때 MapData 객체(mapData)를 생성하여 여기에 스테이지별로 스테이지명과 변환된 지도 데이터를 저장하고 이를 최종적으로 MapData 객체 리스트(mapData_list)에 저장(setter 메서드)하고 이를 반환합니다.
>	> >    + 또한 Sokoban 클래스로부터 2차원 정수형 배열(map_size)을 파라미터로 받고 있는데 이는 스테이지별 가로/세로 크기를 저장하고있으며, 이를 통해 위에서 mapData 객체에 지도 데이터를 저장하기 전에 먼저 지도 데이터를 입력받을 저장할 2차원 배열의 크기를 설정합니다.
>	> >  + showMap 메서드는 위에서 생성된 MapData 객체 리스트를 파라미터로 받아, MapData 객체별로(스테이지별로) 스테이지 명과 지도 데이터를 얻어와(getter) 콘솔창에 출력하는 역할을 합니다.
>	> >    + 이때 얻어오는 지도 데이터는 변환된 값(0~4)으로 저장되어 있었기 때문에 이를 다시 처음 값(#, O, o 등)으로 되돌려 주어야 합니다.
>	> >    + 이를 위해 별도 getMapData 메서드를 통해 value(0~4)에 해당하는 key(#, O, o 등)이 존재하는 경우 key를 얻고, 해당되는 게 없는 경우 ' '(공백)을 얻어왔습니다.
> > >  ```Java
> > >  // hashmap의 value 로 key 찾기
> > >  public char getMapData(int value) {
> > >  
> > >  // value와 일치하는 key 반환
> > >  for (Character key : this.map_data_value.keySet()) {
> > >       if (value == this.map_data_value.get(key)) {
> > >           return key;
> > >       }
> > >   }
> > >   // value와 일치하는 key가 없는 경우 ' ' 반환
> > >   return ' ';
> > >  }
> > >  ```
>	>
>	> + #### MapData 클래스(스테이지별 지도 데이터 저장 및 )
>	> >  + MapData 클래스는 콘솔창으로부터 입력받은 데이터를 저장하고 이를 가공, 처리하여 콘솔창에 출력하는 과정에서 "저장"과 "전송"의 역할을 하고있습니다.
>	> >  + 문자열 stage_name 필드는 스테이지명(Stage 1, Stage 2, ...)을 저장할 때 2차원 정수형 배열 map_data 필드는 변환된 지도 데이터 값(0~4)를 저장할 때 사용됩니다.
>	> >    + 이때 MapData 객체에 데이터를 저장할 때는 setter 메서드를 사용했고 데이터를 전송할 때는 getter 메서드를 사용했습니다.
> > > ```Java
> > >     private String stage_name;
> > >     private int[][] map_data;
> > > 
> > >     public String getStage_name() {
> > >         return stage_name;
> > >     }
> > > 
> > >     public int[][] getMap_data() {
> > >         return map_data;
> > >     }
> > > 
> > >     public void setStage_name(String stage_name) {
> > >         this.stage_name = stage_name;
> > >     }
> > > 
> > >     public void setMap_data(int[][] map_data) {
> > >         this.map_data = map_data;
> > >     }
> > > ```
  
<br/>
<hr/>
<br/>

> ### 실행 결과
> > #### 입력
> > >  ```
> > > Stage 1
> > > #####
> > > #OoP#
> > > #####
> > > =====
> > > Stage 2
> > > #######
> > > ###  O  ###
> > > #    o    #
> > > # Oo P oO #
> > > ###  o  ###
> > > #   O  # 
> > > ########
> > > ```
> > #### 출력
> > > ```
> > > Stage 1
> > > 
> > > #####
> > > #OoP#
> > > #####
> > > 
> > > 가로크기: 5
> > > 세로크기: 3
> > > 구멍의 수: 1
> > > 공의 수: 1
> > > 플레이어 위치 (2, 4)
> > > 
> > > Stage 2
> > > 
> > >   #######  
> > > ###  O  ###
> > > #    o    #
> > > # Oo P oO #
> > > ###  o  ###
> > >  #   O  #  
> > >  ########  
> > > 
> > > 가로크기: 11
> > > 세로크기: 7
> > > 구멍의 수: 4
> > > 공의 수: 4
> > > 플레이어 위치 (4, 6)
> > > 
> > > ```

<br/>
<hr/>
<br/>
  
## 2단계

## 3단계