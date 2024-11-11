import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Witaj! Ten program obliczy średnią ocen oraz sprawdzi, czy przysługuje stypendium naukowe.");
        System.out.print("Podaj liczbę ocen, które chcesz wprowadzić: ");
        int liczbaOcen = scanner.nextInt();

        if (liczbaOcen <= 0) {
            System.out.println("Błędna liczba ocen.");
            return;
        }

        double sumaOcen = 0;
        for (int i = 1; i <= liczbaOcen; i++) {
            double ocena;
            do {
                System.out.print("Podaj ocenę nr " + i + " (od 3 do 5): ");
                ocena = scanner.nextDouble();
                if (ocena < 3 || ocena > 5) {
                    System.out.println("Ocena spoza zakresu! Podaj ocenę ponownie.");
                }
            } while (ocena < 3 || ocena > 5);

            sumaOcen += ocena;
        }

        double srednia = sumaOcen / liczbaOcen;
        System.out.println("Twoja średnia ocen to: " + srednia);

        if (srednia > 4.1) {
            System.out.println("Gratulacje! Przysługuje Ci stypendium naukowe.");
        } else {
            System.out.println("Nie przysługuje Ci stypendium naukowe.");
        }

        scanner.close();
    }
}
