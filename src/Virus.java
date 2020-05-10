

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Virus {
    public boolean isAutoPickupEnabled = false;
    public int mutationDelay = 300;
    public int lifeTime = 0;
    public double innerInfectabilityRate = 0.005;
    public double medicineEffect = 0;
    public double mortality = 0.001;
    public double popupChance = 0.003;
    public double[] transportWayInfectability; //LAND,AIR,SEA
    public double[] climateInfectability; //COLD,MEDIOCRE,HOT
    public double [] transportationChances;
    public HashMap<Integer, List<Mutation>> mutations;
    public Virus(){
         mutations = new HashMap<>();
         initMutations();

         transportWayInfectability = new double[]{1,1,1};
         climateInfectability = new double[]{1,1,1};
         transportationChances = new double[]{0.5,0.5,0.5};

        //initial infection
         Main.countries.get((int) (Math.random()*Main.countries.size())).infected = 50;


    }
    public void initMutations(){
        Mutation coldClimateInfectabilityIncreased = new Mutation("Increased virus spread in cold climate", 0){
            @Override
            public void apply(){
                super.apply();
                climateInfectability[0] += 0.12;
            }
        };
        Mutation mediocreClimateInfectabilityIncreased = new Mutation("Increased virus spread in mediocre climate", 0){
            @Override
            public void apply(){
                super.apply();
                climateInfectability[1] += 0.15;
            }
        };
        Mutation hotClimateInfectabilityIncreased = new Mutation("Increased virus spread in hot climate", 0){
            @Override
            public void apply(){
                super.apply();
                climateInfectability[2] += 0.1;
            }
        };


        Mutation landInfectabilityIncreased = new Mutation("Increased virus spread via land travel", 1,
                hotClimateInfectabilityIncreased,
                mediocreClimateInfectabilityIncreased,
                coldClimateInfectabilityIncreased){
            @Override
            public void apply(){
                super.apply();
                transportWayInfectability[0] += 0.2;
            }
        };;
        Mutation landInfectabilityIncreased2 = new Mutation("Increased virus spread via land travel II", 2, landInfectabilityIncreased){
            @Override
            public void apply(){
                super.apply();
                transportWayInfectability[0] += 0.3;
            }
        };;;
        Mutation landInfectabilityIncreased3 = new Mutation("Increased virus spread via land travel III", 3, landInfectabilityIncreased2)
        {
            @Override
            public void apply(){
                super.apply();
                transportWayInfectability[0] += 0.5;
            }
        };;;
        Mutation airInfectabilityIncreased = new Mutation("Increased virus spread via air travel", 1,
                hotClimateInfectabilityIncreased,
                mediocreClimateInfectabilityIncreased,
                coldClimateInfectabilityIncreased){
            @Override
            public void apply(){
                super.apply();
                transportWayInfectability[1] += 0.2;
            }
        };
        Mutation airInfectabilityIncreased2 = new Mutation("Increased virus spread via air travel II", 2, airInfectabilityIncreased){
            @Override
            public void apply(){
                super.apply();
                transportWayInfectability[1] += 0.3;
            }
        };
        Mutation airInfectabilityIncreased3 = new Mutation("Increased virus spread via air travel III", 3, airInfectabilityIncreased2){
            @Override
            public void apply(){
                super.apply();
                transportWayInfectability[1] += 0.5;
            }
        };
        Mutation mutationSpeedIncrease = new Mutation("Increased rate of mutations by "+(int)((mutationDelay/200-1)*100)+"%", 4, airInfectabilityIncreased3, landInfectabilityIncreased3){
            @Override
            public void apply(){
                super.apply();
                Main.v.mutationDelay = 200;
            }
        };
        Mutation mortalityIncrease = new Mutation("Increased mortality", 4, airInfectabilityIncreased3, landInfectabilityIncreased3){
            @Override
            public void apply(){
                super.apply();
                Main.v.mortality *=2;
            }
        };

        //E N D  O F  V I R U S  M U T A T I O N S//

        Mutation medicineImprovement = new Mutation("Buy more medical equipment", 0, 16,true){
            @Override
            public void apply(){
                super.apply();
                Main.v.medicineEffect+=0.001;
            }
        };

        Mutation medicineImprovement2 = new Mutation("Provide masks", 1, 36,true, medicineImprovement){
            @Override
            public void apply(){
                super.apply();
                Main.v.medicineEffect+=0.001;
            }
        };

        Mutation bonusPopupFreqIncrease = new Mutation("Increases frequency of popup bonuses",0,17,true) {
            @Override
            public void apply() {
                super.apply();
                Main.v.popupChance += 0.001;
            }
        };
        Mutation investigationBegin = new Mutation("Start investigating the virus in labs", 2,21, true,medicineImprovement2);

        Mutation increaseMaxPopupValue = new Mutation("Increase amount of OmegaCoins in bonuses", 2, 25, true, bonusPopupFreqIncrease){
            @Override
            public void apply() {
                super.apply();
                Country.BonusPopup.maxValue += 1;
            }
        };
        Mutation reduceTransportRate = new Mutation("Discourage travelling",3,29, true, investigationBegin,increaseMaxPopupValue){
            @Override
            public void apply() {
                super.apply();
                Main.v.transportationChances[0] -= 0.03;
                Main.v.transportationChances[1] -= 0.03;
                Main.v.transportationChances[2] -= 0.02;
            }
        };
        Mutation increaseMaxPopupValue2 = new Mutation("Increase amount of OmegaCoins in bonuses", 3, 31, true, investigationBegin,increaseMaxPopupValue){
            @Override
            public void apply() {
                super.apply();
                Country.BonusPopup.maxValue += 2;
            }
        };
        Mutation reduceAirTransport = new Mutation("Reduces plane traffic", 4, 35, true, reduceTransportRate){
            @Override
            public void apply() {
                super.apply();
                Main.v.transportationChances[1] -= 0.05;
            }
        };
        Mutation reduceWaterTransport = new Mutation("Reduces water traffic", 4, 40, true, reduceTransportRate){
            @Override
            public void apply() {
                super.apply();
                Main.v.transportationChances[2] -= 0.06;
            }
        };
        Mutation encourageInnerQuarantine = new Mutation("Encourage self-quarantine", 4, 29, true,reduceTransportRate){
            @Override
            public void apply() {
                super.apply();
                Main.v.innerInfectabilityRate-=0.001;
            }
        };

        Mutation enableCuring = new Mutation("Start curing the infected", 5, 200, true, encourageInnerQuarantine){
            @Override
            public void apply() {
                super.apply();
                Main.v.medicineEffect += 0.045;
            }
        };
        Mutation totalQuarantine = new Mutation("Establish quarantine", 5, 230, true,reduceAirTransport, encourageInnerQuarantine){
            @Override
            public void apply() {
                super.apply();
                Main.v.innerInfectabilityRate = 0.001;
                Main.v.transportationChances = new double[]{0.05,0.05,0.05};
            }
        };
        Mutation enableAutoPickup = new Mutation("Automatically picks up Bonuses", 5, 130, true, reduceWaterTransport, encourageInnerQuarantine){
            @Override
            public void apply() {
                super.apply();
                Main.v.isAutoPickupEnabled = true;
            }
        };

        mutations = (HashMap<Integer, List<Mutation>>) Stream.of(
                hotClimateInfectabilityIncreased,
                mediocreClimateInfectabilityIncreased,
                coldClimateInfectabilityIncreased,
                landInfectabilityIncreased,
                landInfectabilityIncreased2,
                landInfectabilityIncreased3,
                airInfectabilityIncreased,
                airInfectabilityIncreased2,
                airInfectabilityIncreased3,
                mortalityIncrease,
                mutationSpeedIncrease).
                collect(Collectors.groupingBy((Mutation m)->m.level));

        Main.upgrades = (HashMap<Integer, List<Mutation>>) Stream.of(
                medicineImprovement,
                medicineImprovement2,
                reduceAirTransport,
                reduceWaterTransport,
                bonusPopupFreqIncrease,
                investigationBegin,
                increaseMaxPopupValue,
                reduceTransportRate,
                encourageInnerQuarantine,
                increaseMaxPopupValue2,
                totalQuarantine,
                enableCuring,
                enableAutoPickup)
                .collect(Collectors.groupingBy((Mutation m)->m.level));
    }

}