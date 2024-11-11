import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
abstract class Field {
    protected String name;
    protected int id;

    public Field(String fieldName, int fieldID) {
        name = fieldName;
        id = fieldID;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void performAction(List<Player> players, int currentPlayer, int totalDice);
}

class Player {
    private String name;
    private int position;
    private int money;

    public Player(String playerName) {
        name = playerName;
        money = 1500;
        position = 0;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int newPosition) {
        position = newPosition;
    }

    public void deductMoney(int amount) {
        money -= amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }




}

class PropertyField extends Field {
    private int cost;
    private int rent;
    private int houseTax;
    private int hotelTax;
    private int owner;
    private int houses;
    private int hotels;

    public PropertyField(String fieldName, int fieldId, int fieldCost, int fieldRent, int fieldHouseTax, int fieldHotelTax) {
        super(fieldName, fieldId);
        cost = fieldCost;
        rent = fieldRent;
        houseTax = fieldHouseTax;
        hotelTax = fieldHotelTax;
        owner = -1;
        houses = 0;
        hotels = 0;
    }

    public int getCost() {
        return cost;
    }

    public int getRent() {
        return rent;
    }

    public int getHouseTax() {
        return houseTax;
    }

    public int getHotelTax() {
        return hotelTax;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int playerIndex) {
        owner = playerIndex;
    }

    public int getHouses() {
        return houses;
    }

    public void addHouse() {
        houses++;
    }

    public int getHotels() {
        return hotels;
    }

    public void addHotel() {
        hotels++;
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znajduje się na " + name + ".");

        if (owner == -1) {
            int option = JOptionPane.showConfirmDialog(null, "Czy chcesz kupić " + getName() + " za $" + cost + "?", "Wybierz opcję", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION)

            {
                if (currentPlayer.getMoney() >= cost) {
                    currentPlayer.deductMoney(cost);
                    setOwner(currentPlayerIndex);
                    JOptionPane.showMessageDialog(null, "Gratulacje! Zostałeś właścicielem pola " + getName() + ".");
                } else {
                    JOptionPane.showMessageDialog(null, "Nie posiadasz wystarczających środków na zakup " + getName() + ".");
                }

            }
        } else if (owner != currentPlayerIndex) {
            int rentAmount = rent + (getHouses() * getHouseTax()) + (getHotels() * getHotelTax());
            currentPlayer.deductMoney(rentAmount);
            players.get(getOwner()).addMoney(rentAmount);
            JOptionPane.showMessageDialog(null, "Podatek w wysokości " + rentAmount + "$ został przekazany graczowi " + players.get(getOwner()).getName() + ".");
        }
    }
}

class ChanceField extends Field {
    public ChanceField(String fieldName, int fieldId) {
        super(fieldName, fieldId);
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znajduje się na polu " + getName() + ".");
        int amount = (int) (Math.random() * 100) + 1;
        currentPlayer.addMoney(amount);
        JOptionPane.showMessageDialog(null, "Dobrałeś kartę szansy. Otrzymujesz $" + amount + ".");
    }
}

class TaxField extends Field {
    private int taxAmount;

    public TaxField(String fieldName, int fieldId, int taxAmount) {
        super(fieldName, fieldId);
        this.taxAmount = taxAmount;
    }

    public int getTaxAmount() {
        return taxAmount;
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znalazł się na polu " + getName() + ".");
        JOptionPane.showMessageDialog(null, "Zapłacono podatek w wysokości " + taxAmount + " zł.");
        currentPlayer.deductMoney(taxAmount);
    }
}


class RiskField extends Field {
    public RiskField(String fieldName, int fieldId) {
        super(fieldName, fieldId);
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znajduje się na polu " + getName() + ".");
        int amount = (int) (Math.random() * 100) + 1;
        currentPlayer.deductMoney(amount);
        JOptionPane.showMessageDialog(null, "Dobrałeś kartę ryzyka. Straciłeś $" + amount + ".");
    }
}

class WaterworksField extends Field {
    private int cost;
    private int owner;

    public WaterworksField(String fieldName, int fieldId, int fieldCost) {
        super(fieldName, fieldId);
        cost = fieldCost;
        owner = -1;
    }

    public int getCost() {
        return cost;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int playerIndex) {
        owner = playerIndex;
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znajduje się na " + getName() + ".");

        if (owner == -1) {
            int option = JOptionPane.showConfirmDialog(null, "Czy chcesz kupić " + getName() + " za $" + cost + "?", "Wybierz opcję", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {

                {
                    if (currentPlayer.getMoney() >= cost) {
                        currentPlayer.deductMoney(cost);
                        setOwner(currentPlayerIndex);
                        JOptionPane.showMessageDialog(null, "Gratulacje! Zostałeś właścicielem pola " + getName() + ".");
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie posiadasz wystarczających środków na zakup " + getName() + ".");
                    }
                }
            }
        } else if (owner != currentPlayerIndex) {
            int rentAmount = totalDice * 10;
            currentPlayer.deductMoney(rentAmount);
            players.get(getOwner()).addMoney(rentAmount);
            JOptionPane.showMessageDialog(null, "Podatek w wysokości " + rentAmount + "$ został przekazany graczowi " + players.get(getOwner()).getName() + ".");
        }
    }
}

class PowerPlantField extends Field {
    private int cost;
    private int owner;

    public PowerPlantField(String fieldName, int fieldId, int fieldCost) {
        super(fieldName, fieldId);
        cost = fieldCost;
        owner = -1;
    }

    public int getCost() {
        return cost;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int playerIndex) {
        owner = playerIndex;
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znajduje się na " + getName() + ".");

        if (owner == -1) {
            int option = JOptionPane.showConfirmDialog(null, "Czy chcesz kupić " + getName() + " za $" + cost + "?", "Wybierz opcję", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (currentPlayer.getMoney() >= cost) {
                    currentPlayer.deductMoney(cost);
                    setOwner(currentPlayerIndex);
                    JOptionPane.showMessageDialog(null, "Gratulacje! Zostałeś właścicielem pola " + getName() + ".");
                } else {
                    JOptionPane.showMessageDialog(null, "Nie posiadasz wystarczających środków na zakup " + getName() + ".");
                }
            }
        } else if (owner != currentPlayerIndex) {
            int rentAmount = totalDice * 10;
            currentPlayer.deductMoney(rentAmount);
            players.get(getOwner()).addMoney(rentAmount);
            JOptionPane.showMessageDialog(null, "Podatek w wysokości " + rentAmount + "$ został przekazany graczowi " + players.get(getOwner()).getName() + ".");
        }
    }

}

class RailwayField extends Field {
    private int cost;
    private int owner;

    public RailwayField(String fieldName, int fieldId, int fieldCost) {
        super(fieldName, fieldId);
        cost = fieldCost;
        owner = -1;
    }

    public int getCost() {
        return cost;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int playerIndex) {
        owner = playerIndex;
    }

    @Override
    public void performAction(List<Player> players, int currentPlayerIndex, int totalDice) {
        Player currentPlayer = players.get(currentPlayerIndex);
        JOptionPane.showMessageDialog(null, "Gracz " + currentPlayer.getName() + " znajduje się na " + getName() + ".");

        if (owner == -1) {
            int option = JOptionPane.showConfirmDialog(null, "Czy chcesz kupić " + getName() + " za $" + cost + "?", "Wybierz opcję", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                ;
                {
                    if (currentPlayer.getMoney() >= cost) {
                        currentPlayer.deductMoney(cost);
                        setOwner(currentPlayerIndex);
                        JOptionPane.showMessageDialog(null, "Gratulacje! Zostałeś właścicielem pola " + getName() + ".");
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie posiadasz wystarczających środków na zakup " + getName() + ".");
                    }
                }
            }
        } else if (owner != currentPlayerIndex) {
            int rentAmount = totalDice * 10;
            currentPlayer.deductMoney(rentAmount);
            players.get(getOwner()).addMoney(rentAmount);
            JOptionPane.showMessageDialog(null, "Podatek w wysokości " + rentAmount + "$ został przekazany graczowi " + players.get(getOwner()).getName() + ".");
        }
    }
}

class STARTField extends Field {
    public STARTField(String fieldName, int fieldId) {
        super(fieldName, fieldId);
    }

    @Override
    public void performAction(List<Player> players, int currentPlayer, int totalDice) {
        int bonusAmount = 200;
        players.get(currentPlayer).addMoney(bonusAmount);
        JOptionPane.showMessageDialog(null,"Otrzymujesz $200 premii za przekroczenie!");
    }
}

class Banker {
    private int money;

    public Banker(int initialMoney) {
        money = initialMoney;
    }

    public void depositMoney(int amount) {
        money += amount;
    }

    public void withdrawMoney(int amount) {
        money -= amount;
    }

    public int getBalance() {
        return money;
    }
}

public class Main {
    public static void performFieldAction(List<Player> players, int currentPlayerIndex, List<Field> gameFields, int totalDice) {
        int currentPosition = players.get(currentPlayerIndex).getPosition();
        currentPosition = (currentPosition + totalDice) % gameFields.size();
        Field currentField = gameFields.get(currentPosition);
        players.get(currentPlayerIndex).setPosition(currentPosition);
        currentField.performAction(players, currentPlayerIndex, totalDice);
    }
    public static void startNewGame() {
        int numPlayers;
        do {
            numPlayers = Integer.parseInt(JOptionPane.showInputDialog("Wpisz liczbe graczy, ktorzy wezma udzial w rozgrywce (2-6):"));
        } while (numPlayers < 2 || numPlayers > 6);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            String playerName = JOptionPane.showInputDialog("Podaj nazwe Gracza " + i + ":");
            players.add(new Player(playerName));
        }

        List<Field> gameFields = new ArrayList<>();
        gameFields.add(new STARTField("START", 0));
        gameFields.add(new PropertyField("Saloniki", 1, 120, 10, 40, 500));
        gameFields.add(new TaxField("Podatek", 36, 200));
        gameFields.add(new ChanceField("Pole Szansy [1]", 2));
        gameFields.add(new PropertyField("Ateny", 3, 120, 10, 40, 500));
        gameFields.add(new PropertyField("Olimp", 4, 120, 10, 40, 500));
        gameFields.add(new RailwayField("Linie kolejowe Poludniowe", 5, 400));
        gameFields.add(new PropertyField("Neapol", 6, 200, 15, 60, 400));
        gameFields.add(new RiskField("Pole Ryzyka [1]", 7));
        gameFields.add(new PropertyField("Mediolan", 8, 200, 15, 60, 400));
        gameFields.add(new PropertyField("Rzym", 9, 240, 20, 80, 400));
        gameFields.add(new PropertyField("Barcelona", 10, 280, 20, 100, 1500));
        gameFields.add(new PowerPlantField("Elektrownia atomowa", 11, 300));
        gameFields.add(new PropertyField("Sewilla", 12, 280, 20, 100, 1500));
        gameFields.add(new PropertyField("Madryt", 13, 320, 25, 120, 1800));
        gameFields.add(new RailwayField("Linie kolejowe Zachodnie", 14, 400));
        gameFields.add(new PropertyField("Liverpool", 15, 360, 30, 140, 1900));
        gameFields.add(new ChanceField("Pole Szansy [2]", 16));
        gameFields.add(new PropertyField("Glasgow", 17, 360, 30, 140, 1900));
        gameFields.add(new PropertyField("Londyn", 18, 400, 35, 160, 2000));
        gameFields.add(new PropertyField("Rotterdam", 19, 440, 35, 180, 2100));
        gameFields.add(new RiskField("Pole Ryzyka [2]", 20));
        gameFields.add(new PropertyField("Bruksela", 21, 440, 35, 180, 2100));
        gameFields.add(new PropertyField("Amsterdam", 22, 480, 40, 200, 2200));
        gameFields.add(new RailwayField("Linie kolejowe Polnocne", 23, 400));
        gameFields.add(new PropertyField("Malmo", 24, 520, 45, 220, 2200));
        gameFields.add(new PropertyField("Goteborg", 25, 520, 45, 220, 2200));
        gameFields.add(new WaterworksField("Siec wodociagow", 26, 300));
        gameFields.add(new PropertyField("Sztokholm", 27, 560, 50, 240, 2400));
        gameFields.add(new PropertyField("Frankfurt", 28, 600, 55, 260, 900));
        gameFields.add(new PropertyField("Kolonia", 29, 600, 55, 260, 900));
        gameFields.add(new ChanceField("Pole Szansy [3]", 30));
        gameFields.add(new PropertyField("Bonn", 31, 640, 60, 300, 900));
        gameFields.add(new RailwayField("Linie kolejowe Wschodnie", 32, 400));
        gameFields.add(new RiskField("Pole Ryzyka [3]", 33));
        gameFields.add(new PropertyField("Insburck", 34, 700, 70, 350, 1500));
        gameFields.add(new PropertyField("Wieden", 35, 800, 100, 400, 1500));

        int currentPlayer = 0;
        int totalPlayers = players.size();
        boolean gameOver = false;
        Banker banker = new Banker(100000);
        while (true) {
            Player currentPlayerObj = players.get(currentPlayer);

            JOptionPane.showMessageDialog(null, "TURA GRACZA: " + currentPlayerObj.getName()
                    + "\nAKTUALNA POZYCJA: " + currentPlayerObj.getPosition()
                    + "\nSTAN SALDA: " + currentPlayerObj.getMoney());

            int dice1 = (int) (Math.random() * 6) + 1;
            int dice2 = (int) (Math.random() * 6) + 1;
            int totalDice = dice1 + dice2;
            JOptionPane.showMessageDialog(null, "NASTEPUJE RZUT KOSTKAMI: "
                    + "\n" + dice1 + " + " + dice2 + " = " + totalDice + " OCZEK.");

            performFieldAction(players, currentPlayer, gameFields, totalDice);
            JOptionPane.showMessageDialog(null, "\n");

            if (currentPlayerObj.getMoney() <= 0) {
                JOptionPane.showMessageDialog(null, "Gracz " + currentPlayerObj.getName() + " zbankrutowal!");
                totalPlayers--;
                if (totalPlayers == 1) {
                    break;
                }
                players.remove(currentPlayer);
                currentPlayer--;
                for (Player player : players) {
                    if (player.getPosition() > currentPlayerObj.getPosition()) {
                        player.setPosition(player.getPosition() - 1);
                    }
                }
            }
            currentPlayer = (currentPlayer + 1) % totalPlayers;
        }

        JOptionPane.showMessageDialog(null, "KONIEC GRY! GRACZ " + players.get(0).getName() + " ZWYCIEZYL!");
        gameFields.clear();
    }

    public static void main(String[] args) {
        int choice = 0;
        boolean exitGame = false;
        while (!exitGame) {
            String input = JOptionPane.showInputDialog(
                    "1. Nowa gra\n" +
                            "2. Wyjdz z programu\n" +
                            "Wpisz swoj wybor:"
            );
            try {
                choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        startNewGame();
                        break;
                    case 2:
                        exitGame = true;
                        JOptionPane.showMessageDialog(null, "Opuszczasz program MONOPOLY. Zegnaj!");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Wybrales zla opcje. Sproboj ponownie!");
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Podano nieprawidlowy format. Podaj liczbe!");
            }
        }
    }
}