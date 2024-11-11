import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj wartość n: ");
        int n = scanner.nextInt();


        long factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        System.out.println("Wartość " + n + "! = " + factorial);


        double seriesSum = 0;
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) {
                seriesSum -= 1.0 / (i + n);
            } else {
                seriesSum += 1.0 / (i + n);
            }
        }
        System.out.println("Suma ciągu dla n = " + n + " wynosi: " + seriesSum);
    }
}
