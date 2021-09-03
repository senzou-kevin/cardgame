package gamelogic;

import structures.GameState;
import structures.basic.Card;
import structures.basic.Spell;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

/**
 * load human player's cards and ai's cards into memory and store
 * them in the list.
 */
public class LoadCards {
    public static void loadDeckOfCard(GameState gameState){
        //load human player's cards
        ArrayList<Card>player1Deck=new ArrayList<>();
        //load spellCards
        Card c_truestrike1= BasicObjectBuilders.loadCard(StaticConfFiles.c_truestrike,0,Card.class);
        Card c_truestrike2= BasicObjectBuilders.loadCard(StaticConfFiles.c_truestrike,1,Card.class);
        player1Deck.add(c_truestrike1);
        player1Deck.add(c_truestrike2);
        //set spell
        c_truestrike1.setSpell(new Spell(0));
        c_truestrike2.setSpell(new Spell(0));

        //load spell cards
        Card c_sundrop_elixir1=BasicObjectBuilders.loadCard(StaticConfFiles.c_sundrop_elixir,2,Card.class);
        Card c_sundrop_elixir2=BasicObjectBuilders.loadCard(StaticConfFiles.c_sundrop_elixir,3,Card.class);
        player1Deck.add(c_sundrop_elixir1);
        player1Deck.add(c_sundrop_elixir2);
        //set spell
        c_sundrop_elixir1.setSpell(new Spell(1));
        c_sundrop_elixir2.setSpell(new Spell(1));

        //load unit cards
        Card c_comodo_charger1=BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger,4,Card.class);
        Card c_comodo_charger2=BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger,5,Card.class);
        player1Deck.add(c_comodo_charger1);
        player1Deck.add(c_comodo_charger2);
        // set unit
        c_comodo_charger1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger,2, Unit.class));
        c_comodo_charger2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger,3,Unit.class));
        //set scope
        c_comodo_charger1.getUnit().setScope(0,0,0);
        c_comodo_charger2.getUnit().setScope(0,0,0);
        c_comodo_charger1.getUnit().resetAttackTimes(1);
        c_comodo_charger2.getUnit().resetAttackTimes(1);

        //load unit cards
        Card c_azure_herald1=BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald,6,Card.class);
        Card c_azure_herald2=BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald,7,Card.class);
        player1Deck.add(c_azure_herald1);
        player1Deck.add(c_azure_herald2);
        //set unit
        c_azure_herald1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald,4,Unit.class));
        c_azure_herald2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald,5,Unit.class));
        //set scope
        c_azure_herald1.getUnit().setScope(0,0,0);
        c_azure_herald2.getUnit().setScope(0,0,0);
        c_azure_herald1.getUnit().resetAttackTimes(1);
        c_azure_herald2.getUnit().resetAttackTimes(1);

        //load unit cards
        Card c_azurite_lion1=BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion,8,Card.class);
        Card c_azurite_lion2=BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion,9,Card.class);
        player1Deck.add(c_azurite_lion1);
        player1Deck.add(c_azurite_lion2);
        //set unit
        c_azurite_lion1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion,6,Unit.class));
        c_azurite_lion2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion,7,Unit.class));
        //set scope
        c_azurite_lion1.getUnit().setScope(0,0,0);
        c_azurite_lion2.getUnit().setScope(0,0,0);
        c_azurite_lion1.getUnit().resetAttackTimes(2);
        c_azurite_lion2.getUnit().resetAttackTimes(2);

        //load unit cards
        Card c_fire_spitter1=BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter,10,Card.class);
        Card c_fire_spitter2=BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter,11,Card.class);
        player1Deck.add(c_fire_spitter1);
        player1Deck.add(c_fire_spitter2);
        //set unit
        c_fire_spitter1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter,8,Unit.class));
        c_fire_spitter2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter,9,Unit.class));
        //set scope
        c_fire_spitter1.getUnit().setScope(0,1,0);
        c_fire_spitter2.getUnit().setScope(0,1,0);
        c_fire_spitter1.getUnit().resetAttackTimes(1);
        c_fire_spitter2.getUnit().resetAttackTimes(1);

        //load unit cards
        Card c_hailstone_golem1=BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem,12,Card.class);
        Card c_hailstone_golem2=BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem,13,Card.class);
        player1Deck.add(c_hailstone_golem1);
        player1Deck.add(c_hailstone_golem2);
        //set unit
        c_hailstone_golem1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem,10,Unit.class));
        c_hailstone_golem2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem,11,Unit.class));
        //set scope
        c_hailstone_golem1.getUnit().setScope(0,0,0);
        c_hailstone_golem2.getUnit().setScope(0,0,0);
        c_hailstone_golem1.getUnit().resetAttackTimes(1);
        c_hailstone_golem2.getUnit().resetAttackTimes(1);

        //load unit cards
        Card c_ironcliff_guardian1=BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian,14,Card.class);
        Card c_ironcliff_guardian2=BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian,15,Card.class);
        player1Deck.add(c_ironcliff_guardian1);
        player1Deck.add(c_ironcliff_guardian2);
        //set unit
        c_ironcliff_guardian1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian,12,Unit.class));
        c_ironcliff_guardian2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian,13,Unit.class));
        //set scope
        c_ironcliff_guardian1.getUnit().setScope(1,0,0);
        c_ironcliff_guardian2.getUnit().setScope(1,0,0);
        c_ironcliff_guardian1.getUnit().resetAttackTimes(1);
        c_ironcliff_guardian2.getUnit().resetAttackTimes(1);

        //load unit cards
        Card c_pureblade_enforcer1=BasicObjectBuilders.loadCard(StaticConfFiles.c_pureblade_enforcer,16,Card.class);
        Card c_pureblade_enforcer2=BasicObjectBuilders.loadCard(StaticConfFiles.c_pureblade_enforcer,17,Card.class);
        player1Deck.add(c_pureblade_enforcer1);
        player1Deck.add(c_pureblade_enforcer2);
        //set unit
        c_pureblade_enforcer1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer,14,Unit.class));
        c_pureblade_enforcer2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer,15,Unit.class));
        //set scope
        c_pureblade_enforcer1.getUnit().setScope(0,0,0);
        c_pureblade_enforcer2.getUnit().setScope(0,0,0);
        c_pureblade_enforcer1.getUnit().resetAttackTimes(1);
        c_pureblade_enforcer2.getUnit().resetAttackTimes(1);

        //load unit cards
        Card c_silverguard_knight1=BasicObjectBuilders.loadCard(StaticConfFiles.c_silverguard_knight,18,Card.class);
        Card c_silverguard_knight2=BasicObjectBuilders.loadCard(StaticConfFiles.c_silverguard_knight,19,Card.class);
        player1Deck.add(c_silverguard_knight1);
        player1Deck.add(c_silverguard_knight2);
        //set unit
        c_silverguard_knight1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight,16,Unit.class));
        c_silverguard_knight2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight,17,Unit.class));
        //set scope
        c_silverguard_knight1.getUnit().setScope(0,0,0);
        c_silverguard_knight2.getUnit().setScope(0,0,0);
        c_silverguard_knight1.getUnit().resetAttackTimes(1);
        c_silverguard_knight2.getUnit().resetAttackTimes(1);

        //load ai's cards
        ArrayList<Card>aiPlayerDeck=new ArrayList<>();
        //load spell cards
        Card c_staff_of_ykir1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_staff_of_ykir, 0, Card.class);
        Card c_staff_of_ykir2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_staff_of_ykir,1,Card.class);
        aiPlayerDeck.add(c_staff_of_ykir1);
        aiPlayerDeck.add(c_staff_of_ykir2);
        c_staff_of_ykir1.setSpell(new Spell(2));
        c_staff_of_ykir2.setSpell(new Spell(2));
        //load spell cards
        Card c_entropic_decay1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_entropic_decay, 2, Card.class);
        Card c_entropic_decay2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_entropic_decay, 3, Card.class);
        aiPlayerDeck.add(c_entropic_decay1);
        aiPlayerDeck.add(c_entropic_decay2);
        c_entropic_decay1.setSpell(new Spell(3));
        c_entropic_decay1.setSpell(new Spell(3));

        Card c_blaze_hound1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 4, Card.class);
        Card c_blaze_hound2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 5, Card.class);
        c_blaze_hound1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_blaze_hound,18,Unit.class));
        c_blaze_hound2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_blaze_hound,19,Unit.class));
        c_blaze_hound1.getUnit().resetAttackTimes(1);
        c_blaze_hound2.getUnit().resetAttackTimes(1);
        aiPlayerDeck.add(c_blaze_hound1);
        aiPlayerDeck.add(c_blaze_hound2);

        Card c_bloodshard_golem1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_bloodshard_golem, 6, Card.class);
        Card c_bloodshard_golem2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_bloodshard_golem, 7, Card.class);
        c_bloodshard_golem1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_bloodshard_golem,20,Unit.class));
        c_bloodshard_golem2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_bloodshard_golem,21,Unit.class));
        c_bloodshard_golem1.getUnit().resetAttackTimes(1);
        c_bloodshard_golem2.getUnit().resetAttackTimes(1);
        aiPlayerDeck.add(c_bloodshard_golem1);
        aiPlayerDeck.add(c_bloodshard_golem2);

        Card c_planar_scout1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_planar_scout, 8, Card.class);
        Card c_planar_scout2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_planar_scout, 9, Card.class);
        c_planar_scout1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_planar_scout,22,Unit.class));
        c_planar_scout2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_planar_scout,23,Unit.class));
        c_planar_scout1.getUnit().resetAttackTimes(1);
        c_planar_scout2.getUnit().resetAttackTimes(1);
        c_planar_scout1.getUnit().setScope(1,0,0);
        c_planar_scout2.getUnit().setScope(1,0,0);
        aiPlayerDeck.add(c_planar_scout1);
        aiPlayerDeck.add(c_planar_scout2);

        Card c_pyromancer1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_pyromancer, 10, Card.class);
        Card c_pyromancer2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_pyromancer, 11, Card.class);
        c_pyromancer1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer,24,Unit.class));
        c_pyromancer2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer,25,Unit.class));
        c_pyromancer1.getUnit().resetAttackTimes(1);
        c_pyromancer2.getUnit().resetAttackTimes(1);
        c_pyromancer1.getUnit().setScope(0,1,0);
        c_pyromancer2.getUnit().setScope(0,1,0);
        aiPlayerDeck.add(c_pyromancer1);
        aiPlayerDeck.add(c_pyromancer2);

        Card c_rock_pulveriser1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_rock_pulveriser, 12, Card.class);
        Card c_rock_pulveriser2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_rock_pulveriser, 13, Card.class);
        c_rock_pulveriser1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_rock_pulveriser,26,Unit.class));
        c_rock_pulveriser2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_rock_pulveriser,27,Unit.class));
        c_rock_pulveriser1.getUnit().resetAttackTimes(1);
        c_rock_pulveriser2.getUnit().resetAttackTimes(1);
        aiPlayerDeck.add(c_rock_pulveriser1);
        aiPlayerDeck.add(c_rock_pulveriser2);

        Card c_serpenti1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_serpenti, 14, Card.class);
        Card c_serpenti2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_serpenti, 15, Card.class);
        c_serpenti1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti,28,Unit.class));
        c_serpenti2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti,29,Unit.class));
        c_serpenti1.getUnit().resetAttackTimes(2);
        c_serpenti2.getUnit().resetAttackTimes(2);
        aiPlayerDeck.add(c_serpenti1);
        aiPlayerDeck.add(c_serpenti2);

        Card c_windshrike1 = BasicObjectBuilders.loadCard(StaticConfFiles.c_windshrike, 16, Card.class);
        Card c_windshrike2 = BasicObjectBuilders.loadCard(StaticConfFiles.c_windshrike, 17, Card.class);
        c_windshrike1.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike,30,Unit.class));
        c_windshrike2.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike,31,Unit.class));
        c_windshrike1.getUnit().resetAttackTimes(1);
        c_windshrike2.getUnit().resetAttackTimes(1);
        c_windshrike1.getUnit().setScope(0,0,1);
        c_windshrike2.getUnit().setScope(0,0,1);
        aiPlayerDeck.add(c_windshrike1);
        aiPlayerDeck.add(c_windshrike2);

        //load unit cards(a unit card same as player1's )
        Card c_hailstone_golem3=BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem,18,Card.class);
        Card c_hailstone_golem4=BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem,19,Card.class);
        aiPlayerDeck.add(c_hailstone_golem1);
        aiPlayerDeck.add(c_hailstone_golem2);
        //set unit
        c_hailstone_golem3.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem,32,Unit.class));
        c_hailstone_golem4.setUnit(BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem,33,Unit.class));
        //set scope
        c_hailstone_golem3.getUnit().setScope(0,0,0);
        c_hailstone_golem4.getUnit().setScope(0,0,0);
        c_hailstone_golem3.getUnit().resetAttackTimes(1);
        c_hailstone_golem4.getUnit().resetAttackTimes(1);
        gameState.setPlayer1Deck(player1Deck);
        gameState.setAiPlayerDeck(aiPlayerDeck);
    }


}
