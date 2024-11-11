import java.util.Scanner;

class CD {
    String tytulAlbumu;
    String nazwiskoWykonawcy;
    String nazwaWydawcy;
    int rokWydania;
    double cenaPlyty;
    Utwor[] utwory = new Utwor[10];
    int liczbaUtworow = 0;

    void wypelnianie() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tytuł albumu: ");
        tytulAlbumu = scanner.nextLine();
        System.out.print("Nazwisko wykonawcy: ");
        nazwiskoWykonawcy = scanner.nextLine();
        System.out.print("Nazwa wydawcy: ");
        nazwaWydawcy = scanner.nextLine();
        System.out.print("Rok wydania: ");
        rokWydania = scanner.nextInt();
        System.out.print("Cena płyty: ");
        cenaPlyty = scanner.nextDouble();

        char czyDodacUtwor;
        do {
            Utwor nowyUtwor = new Utwor();
            nowyUtwor.wypelnianie();
            dodajUtwor(nowyUtwor);
            System.out.print("Czy dodać kolejny utwór? (T/N): ");
            czyDodacUtwor = scanner.next().charAt(0);
        } while (czyDodacUtwor == 'T' || czyDodacUtwor == 't');
    }

    void dodajUtwor(Utwor nowyUtwor) {
        if (liczbaUtworow < utwory.length) {
            utwory[liczbaUtworow++] = nowyUtwor;
        } else {
            System.out.println("Nie można dodać więcej utworów, limit osiągnięty.");
        }
    }

    void wyswietlanieCD() {
        System.out.println("Tytuł albumu: " + tytulAlbumu);
        System.out.println("Nazwisko wykonawcy: " + nazwiskoWykonawcy);
        System.out.println("Nazwa wydawcy: " + nazwaWydawcy);
        System.out.println("Rok wydania: " + rokWydania);
        System.out.println("Cena płyty: " + cenaPlyty);
        System.out.println("Utwory:");
        for (int i = 0; i < liczbaUtworow; i++) {
            System.out.println("Utwór " + (i + 1) + ":");
            utwory[i].wyswietlUtwor();
            System.out.println();
        }
    }

    void edytujTytul(String nowyTytul) {
        this.tytulAlbumu = nowyTytul;
    }

    void edytujNazwiskoWykonawcy(String noweNazwisko) {
        this.nazwiskoWykonawcy = noweNazwisko;
    }

    void edytujNazwaWydawcy(String nowaNazwa) {
        this.nazwaWydawcy = nowaNazwa;
    }

    void edytujRokWydania(int nowyRok) {
        this.rokWydania = nowyRok;
    }

    void edytujCenaPlyty(double nowaCena) {
        this.cenaPlyty = nowaCena;
    }
}

class Utwor {
    String autor;
    String wykonawca;
    double czasTrwania;


    public void wypelnianie() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Autor utworu: ");
        autor = scanner.nextLine();
        System.out.print("Wykonawca utworu: ");
        wykonawca = scanner.nextLine();
        System.out.print("Czas trwania utworu (min): ");
        czasTrwania = scanner.nextDouble();
    }

    public void wyswietlUtwor() {
        System.out.println("Autor: " + autor);
        System.out.println("Wykonawca: " + wykonawca);
        System.out.println("Czas trwania: " + czasTrwania + " min");
    }

    public void edytujAutora(String nowyAutor) {
        this.autor = nowyAutor;
    }

    public void edytujWykonawce(String nowyWykonawca) {
        this.wykonawca = nowyWykonawca;
    }

    public void edytujCzasTrwania(double nowyCzasTrwania) {
        this.czasTrwania = nowyCzasTrwania;
    }
}

class KolekcjaPlyt {
    CD[] kolekcjaPlyt = new CD[10];
    int liczbaPlyt = 0;

    void dodajPlyte(CD cd) {
        if (liczbaPlyt < kolekcjaPlyt.length) {
            kolekcjaPlyt[liczbaPlyt++] = cd;
        } else {
            System.out.println("Nie można dodać więcej płyt, limit osiągnięty.");
        }
    }

    CD pobierzPlyte(int id) {
        if (id >= 1 && id <= liczbaPlyt) {
            return kolekcjaPlyt[id - 1];
        } else {
            System.out.println("Nie ma płyty o podanym ID.");
            return null;
        }
    }

    double obliczSrednia(String nazwiskoWykonawcy) {
        double sumaCen = 0;
        int iloscPlyt = 0;
        for (int i = 0; i < liczbaPlyt; i++) {
            CD plyta = kolekcjaPlyt[i];
            for (int j = 0; j <plyta.liczbaUtworow; j++){
                Utwor utwor = plyta.utwory[j];
                if (utwor.wykonawca.equalsIgnoreCase(nazwiskoWykonawcy)) {
                    sumaCen += plyta.cenaPlyty;
                    iloscPlyt++;
                    break;
                }
            }

        }
        if (iloscPlyt > 0) {
            return sumaCen / iloscPlyt;
        } else {
            System.out.println("Brak płyt dla podanego wykonawcy.");
            return 0;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        KolekcjaPlyt kolekcja = new KolekcjaPlyt();

        int wybor;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Dodaj płytę do kolekcji");
            System.out.println("2. Wyświetl kolekcję płyt");
            System.out.println("3. Edytuj utwór");
            System.out.println("4. Edytuj płytę");
            System.out.println("0. Wyjście");
            System.out.print("Wybierz opcję: ");
            wybor = scanner.nextInt();
            scanner.nextLine();

            switch (wybor) {
                case 1:
                    CD nowaPlyta = new CD();
                    nowaPlyta.wypelnianie();
                    kolekcja.dodajPlyte(nowaPlyta);
                    break;
                case 2:
                    wyswietlKolekcje(kolekcja);
                    break;
                case 3:
                    edytujUtwor(kolekcja);
                    break;
                case 4:
                    edytujPlyte(kolekcja);
                    break;

                case 0:
                    System.out.println("Koniec programu");
                    break;
                default:
                    System.out.println("Niepoprawna opcja. Wybierz ponownie.");
            }
        } while (wybor != 0);
    }

    public static void wyswietlKolekcje(KolekcjaPlyt kolekcja) {
        System.out.println("\nKolekcja płyt:");
        for (int i = 0; i < kolekcja.liczbaPlyt; i++) {
            System.out.println("Płyta " + (i + 1) + ":");
            kolekcja.kolekcjaPlyt[i].wyswietlanieCD();
        }
    }

    public static void edytujUtwor(KolekcjaPlyt kolekcja) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj numer płyty do edycji (od 1 do " + kolekcja.liczbaPlyt + "): ");
        int numerPlyty = scanner.nextInt();
        scanner.nextLine(); // Konsumowanie znaku nowej linii

        if (numerPlyty < 1 || numerPlyty > kolekcja.liczbaPlyt) {
            System.out.println("Niepoprawny numer płyty.");
            return;
        }

        CD plyta = kolekcja.pobierzPlyte(numerPlyty);
        if (plyta != null) {
            System.out.println("Wybierz utwór do edycji (od 1 do " + plyta.liczbaUtworow + "): ");
            int numerUtworu = scanner.nextInt();
            scanner.nextLine();

            if (numerUtworu < 1 || numerUtworu > plyta.liczbaUtworow) {
                System.out.println("Niepoprawny numer utworu.");
                return;
            }

            Utwor utwor = plyta.utwory[numerUtworu - 1];
            System.out.println("Wybierz pole do edycji:");
            System.out.println("1. Autor utworu");
            System.out.println("2. Wykonawca utworu");
            System.out.println("3. Czas trwania utworu");
            int pole = scanner.nextInt();
            scanner.nextLine();

            switch (pole) {
                case 1:
                    System.out.print("Nowy autor utworu: ");
                    utwor.edytujAutora(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Nowy wykonawca utworu: ");
                    utwor.edytujWykonawce(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Nowy czas trwania utworu (min): ");
                    utwor.edytujCzasTrwania(scanner.nextDouble());
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("Niepoprawny numer pola.");
            }
        }
    }

    public static void edytujPlyte(KolekcjaPlyt kolekcja) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj numer płyty do edycji (od 1 do " + kolekcja.liczbaPlyt + "): ");
        int numerPlyty = scanner.nextInt();
        scanner.nextLine();

        if (numerPlyty < 1 || numerPlyty > kolekcja.liczbaPlyt) {
            System.out.println("Niepoprawny numer płyty.");
            return;
        }

        CD plyta = kolekcja.pobierzPlyte(numerPlyty);
        if (plyta != null) {
            System.out.println("Wybierz pole do edycji:");
            System.out.println("1. Tytuł albumu");
            System.out.println("2. Nazwisko wykonawcy");
            System.out.println("3. Nazwa wydawcy");
            System.out.println("4. Rok wydania");
            System.out.println("5. Cena płyty");
            int pole = scanner.nextInt();
            scanner.nextLine();

            switch (pole) {
                case 1:
                    System.out.print("Nowy tytuł albumu: ");
                    plyta.edytujTytul(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Nowe nazwisko wykonawcy: ");
                    plyta.edytujNazwiskoWykonawcy(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Nowa nazwa wydawcy: ");
                    plyta.edytujNazwaWydawcy(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Nowy rok wydania: ");
                    plyta.edytujRokWydania(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 5:
                    System.out.print("Nowa cena płyty: ");
                    plyta.edytujCenaPlyty(scanner.nextDouble());
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("Niepoprawny numer pola.");
            }
        }
    }
}
