package structures.basic;
import utils.StaticConfFiles;
import java.util.ArrayList;
import java.util.Random;

public class DealCards {
    //牌的种类
    String[] deck1Cards = new String[20];
    String[] deck2Cards = new String[20];
    //card position
    public static int card1Position = 0;
    public static int card2Position = 0;
    //card id
    public static int id1 = 0;
    public static int id2 = 0;

    //已经抽到的卡牌
    public ArrayList<Integer> existsDeck1Cards = new ArrayList<>();
    public ArrayList<Integer> existsDeck2Cards = new ArrayList<>();
    //牌组里的所有牌
    public static ArrayList<String> player1Cards= new ArrayList<>();
    public static ArrayList<String> player2Cards= new ArrayList<>();

    public DealCards() {
        String[] deck1Cards = {
                StaticConfFiles.c_azure_herald,
                StaticConfFiles.c_azurite_lion,
                StaticConfFiles.c_comodo_charger,
                StaticConfFiles.c_fire_spitter,
                StaticConfFiles.c_hailstone_golem,
                StaticConfFiles.c_ironcliff_guardian,
                StaticConfFiles.c_pureblade_enforcer,
                StaticConfFiles.c_silverguard_knight,
                StaticConfFiles.c_sundrop_elixir,
                StaticConfFiles.c_truestrike
        };
    //每种牌存两遍，等于放两张
        for(int i = 0;i<deck1Cards.length;i++){
            player1Cards.add(deck1Cards[i]);
        }
        for(int i = 0;i<deck1Cards.length;i++){
            player1Cards.add(deck1Cards[i]);
        }

        String[] deck2Cards = {
                StaticConfFiles.c_blaze_hound,
                StaticConfFiles.c_bloodshard_golem,
                StaticConfFiles.c_entropic_decay,
                StaticConfFiles.c_hailstone_golem,
                StaticConfFiles.c_planar_scout,
                StaticConfFiles.c_pyromancer,
                StaticConfFiles.c_serpenti,
                StaticConfFiles.c_rock_pulveriser,
                StaticConfFiles.c_staff_of_ykir,
                StaticConfFiles.c_windshrike,
        };

        for(int i = 0;i<deck2Cards.length;i++){
            player2Cards.add(deck2Cards[i]);
        }
        for(int i = 0;i<deck2Cards.length;i++){
            player2Cards.add(deck2Cards[i]);
        }
    }
//  抽牌
    public String player1GetCard(){

        Random random = new Random();
        int number = 0;
        //将随机到的数字与已存在的数字相匹配，如果存在过，则发牌重新随机
//        while(existsDeck1Cards.contains(number)){
//            number = random.nextInt(20);
//        }
//        existsDeck1Cards.add(number);
//        return player1Cards.get(number);
          number = random.nextInt(21);
          while (player1Cards.get(number).isEmpty()){
            number = random.nextInt(21);
          }
          String card = player1Cards.get(number);
          player1Cards.remove(number);
          return card;
    }


    public String player2GetCard(){
//        Random random = new Random();
//        int number = 0;
//
//        //将随机到的数字与已存在的数字相匹配，如果存在过，则发牌重新随机
//        while(existsDeck2Cards.contains(number)){
//            number = random.nextInt(20);
//        }
//
//        existsDeck2Cards.add(number);
//        return player2Cards.get(number);
        return null;
    }

}