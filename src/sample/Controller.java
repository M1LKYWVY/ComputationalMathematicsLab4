package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    public TextField functionField;
    @FXML
    public TextField x0Field;
    @FXML
    public TextField y0Field;
    @FXML
    public TextField xEndField;
    @FXML
    public TextField accuracyField;
    @FXML
    public CheckBox difFunctionCheckBox;
    @FXML
    public CheckBox functionCheckBox;
    @FXML
    public LineChart<Number, Number> numberLineChart;
    private String currentGraphic = "";
    private String currentFrom = "";
    private String currentY0 = "";
    private String currentTo = "";
    private String currentAccuracy = "";
    private boolean isBuilt = false;
    private ArrayList<XYChart.Series<Number, Number>> functionsList = new ArrayList<>();

    @FXML
    public void solveODE(ActionEvent actionEvent) {
        double x0, y0, xN, accuracy;
        try {
            x0 = Double.parseDouble(x0Field.getText());
            y0 = Double.parseDouble(y0Field.getText());
            xN = Double.parseDouble(xEndField.getText());
            accuracy = Double.parseDouble(accuracyField.getText());
            if (accuracy<0.01) riseAlert("Warning", "Too small accuracy", "Check input data. Error has risen.");
            if (xN < x0 ||
                    accuracy <= 0 ||
                    accuracy > xN) throw new Exception();


        } catch (Exception ignored) {
            riseAlert("Warning", "Error in parsing data", "Check input data. Error has risen.");
            return;
        }
        if (currentGraphic.equals(functionField.getText())
                && currentFrom.equals(x0Field.getText())
                && currentTo.equals(xEndField.getText())
                && currentAccuracy.equals(accuracyField.getText())
                && currentY0.equals(y0Field.getText())) {
            return;
        } else {
            currentGraphic = functionField.getText();
            currentFrom = x0Field.getText();
            currentTo = xEndField.getText();
            currentAccuracy = accuracyField.getText();
            if (isBuilt) {
                numberLineChart.getData().remove(0);
            }
        }
        ArrayList<Double> xDifList = new ArrayList<>();
        ArrayList<Double> yDifList = new ArrayList<>();
        xDifList.add(x0);
        yDifList.add(y0);
        double step = accuracy;
        int n = 1;
        try {
        for (double i = x0 + step; i <= xN; i += step) {
            xDifList.add(i);
            switch (n) {
                case 1: {
                    yDifList.add(yDifList.get(yDifList.size() - 1)
                            + step
                            * getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 1), yDifList.get(yDifList.size() - 1)));
                    n++;
                    break;
                }
                case 2: {
                    yDifList.add(yDifList.get(yDifList.size() - 1)
                            + step
                            * (1.5*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 1), yDifList.get(yDifList.size() - 1)))
                            - 0.5*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 2), yDifList.get(yDifList.size() - 2)));
                    n++;
                    break;
                }
                case 3: {
                    yDifList.add(yDifList.get(yDifList.size() - 1)
                            + step
                            * (23/12*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 1), yDifList.get(yDifList.size() - 1)))
                            - 4/3*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 2), yDifList.get(yDifList.size() - 2))
                            + 5/12*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 3), yDifList.get(yDifList.size() - 3)));
                    n++;
                    break;
                }
                case 4: {
                    yDifList.add(yDifList.get(yDifList.size() - 1)
                            + step
                            * (55/24*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 1), yDifList.get(yDifList.size() - 1)))
                            - 59/24*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 2), yDifList.get(yDifList.size() - 2))
                            + 37/24*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 3), yDifList.get(yDifList.size() - 3))
                            - 3/8*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 4), yDifList.get(yDifList.size() - 4)));
                    n++;
                    break;
                }
                case 5: {
                    yDifList.add(yDifList.get(yDifList.size() - 1)
                            + step
                            * (1901/720*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 1), yDifList.get(yDifList.size() - 1)))
                            - 1387/360*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 2), yDifList.get(yDifList.size() - 2))
                            + 109/30*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 3), yDifList.get(yDifList.size() - 3))
                            - 637/360*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 4), yDifList.get(yDifList.size() - 4))
                            + 251/720*getFunctionValue(functionField.getText(), xDifList.get(xDifList.size() - 5), yDifList.get(yDifList.size() - 5)));
                    n=1;
                    break;
                }
            }
        }
    }
    catch (Exception ignored) {
        riseAlert("Warning", "Error in computing data", "Check input data. Error has risen.");
        return;
    }
        double x[], y[], h[], l[], delta[], lambda[], c[], d[], b[];

        int N = xDifList.size();

        x = new double[N];
        y = new double[N];
        h = new double[N];
        l = new double[N];
        delta = new double[N];
        lambda = new double[N];
        c = new double[N];
        d = new double[N];
        b = new double[N];

        for (int i = 0; i < N; ++i) {
            x[i] = xDifList.get(i);
            y[i] = yDifList.get(i);
        }

        for (int k = 1; k < N; k++) {
            h[k] = x[k] - x[k - 1];
            if (h[k] == 0) {
                System.out.printf("\nError, x[%d]=x[%d]\n", k, k - 1);
                return;
            }
            l[k] = (y[k] - y[k - 1]) / h[k];
        }
        delta[1] = -h[2] / (2 * (h[1] + h[2]));
        lambda[1] = 1.5 * (l[2] - l[1]) / (h[1] + h[2]);
        for (int k = 3; k < N; k++) {
            delta[k - 1] = -h[k] / (2 * h[k - 1] + 2 * h[k] + h[k - 1] * delta[k - 2]);
            lambda[k - 1] = (3 * l[k] - 3 * l[k - 1] - h[k - 1] * lambda[k - 2]) /
                    (2 * h[k - 1] + 2 * h[k] + h[k - 1] * delta[k - 2]);
        }

        c[0] = 0;
        c[N - 1] = 0;
        for (int k = N - 1; k >= 2; k--) {
            c[k - 1] = delta[k - 1] * c[k] + lambda[k - 1];
        }
        for (int k = 1; k < N; k++) {
            d[k] = (c[k] - c[k - 1]) / (3 * h[k]);
            b[k] = l[k] + (2 * c[k] * h[k] + h[k] * c[k - 1]) / 3;
        }
        ArrayList<String> splineFunctions = new ArrayList<>();
        for (int i = 1; i < N; ++i) {
            String bSign, cSign, dSign;
            bSign = "";
            cSign = "";
            dSign = "";
            if (b[i] >= 0) {
                bSign = "+";
            }
            if (c[i] >= 0) {
                cSign = "+";
            }

            if (d[i] >= 0) {
                dSign = "+";
            }

            splineFunctions.add(y[i] + bSign + b[i] + "*" + "(x-" + x[i] + ")" + cSign + c[i] + "*" + "(x-" + x[i] + ")^2" + dSign + d[i] + "*" + "(x-" + x[i] + ")^3");
        }
        System.out.println("\nf(x)=" + currentGraphic +
                "\na[i]\tb[i]\tc[i]\td[i]\n");
        for (int k = 1; k < N; k++) {
            System.out.printf("%f\t%f\t%f\t%f\n", y[k], b[k], c[k], d[k]);
        }
        XYChart.Series<Number, Number> functionSeries = new XYChart.Series<>();
        functionSeries.setName("Function");
        for (String function: splineFunctions) {
            System.out.println("\nf(x): " + function);
        }
        step = step/10;
        for (int i = 0; i < splineFunctions.size(); ++i) {
            for (double j = xDifList.get(i); j <= xDifList.get(i + 1); j += step) {
                try {
                    functionSeries.getData().add(new XYChart.Data<>(j, getFunctionValue(splineFunctions.get(i), j)));
                } catch (Exception ignored) {
                    riseAlert("Warning", "Error in computing spline Function", "Check input data. Error has risen.");
                    return;
                }
            }
        }
        numberLineChart.setCreateSymbols(false);
        numberLineChart.getData().add(functionSeries);
        isBuilt = true;
    }

    @FXML
    public void modifyFunc(ActionEvent actionEvent) {
        if (!isBuilt) {
            return;
        }
        if (actionEvent.getSource() instanceof CheckBox) {
            CheckBox chk = (CheckBox) actionEvent.getSource();
            boolean newValue = chk.isSelected();
            switch (chk.getText()) {
                case "Function y'(x)": {
                    int index = 0;
                    for (XYChart.Series<Number, Number> series1: functionsList) {
                        if (series1.getName().equals("Function")) {
                            index = functionsList.indexOf(series1);
                        }
                    }

                    if (newValue) {
                        numberLineChart.getData().add(functionsList.get(index));
                    }
                    else {
                        for (XYChart.Series<Number, Number> series1: numberLineChart.getData()) {
                            if (series1.getName().equals("Function")) {
                                index = numberLineChart.getData().indexOf(series1);
                            }
                        }
                        numberLineChart.getData().remove(index);
                    }

                    break;
                }
                case "Function y(x)": {
                    int index = 0;
                    for (XYChart.Series<Number, Number> series1: functionsList) {
                        if (series1.getName().equals("Spline function")) {
                            index = functionsList.indexOf(series1);
                        }
                    }

                    if (newValue) {
                        numberLineChart.getData().add(functionsList.get(index));
                    }
                    else {
                        for (XYChart.Series<Number, Number> series1: numberLineChart.getData()) {
                            if (series1.getName().equals("Spline function")) {
                                index = numberLineChart.getData().indexOf(series1);
                            }
                        }
                        numberLineChart.getData().remove(index);
                    }
                    break;
                }
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void riseAlert(final String title, final String header,
                           final String content) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        if (title == null || title.isEmpty()) return;
        alert.setTitle(title);
        if (header != null && !header.isEmpty()) alert.setHeaderText(header);
        if (content != null && !content.isEmpty()) alert.setContentText(content);
        alert.showAndWait();
    }

    private double getFunctionValue(String function, double x, double y) throws Exception {
        Expression ex = new ExpressionBuilder(function).variables("x", "y").build();
        ex.setVariable("x", x);
        ex.setVariable("y", y);
        ValidationResult res = ex.validate();
        if (!res.isValid()) {
            for (String err : res.getErrors()) {
                System.out.println(err);
            }
        }
        double evaluation = ex.evaluate();
        if (Double.isNaN(evaluation) || Double.isInfinite(evaluation)) throw new Exception();
        return evaluation;
    }

    private double getFunctionValue(String function, double x) throws Exception{
        Expression ex = new ExpressionBuilder(function).variables("x").build();
        ex.setVariable("x", x);
        ValidationResult res = ex.validate();
        if (!res.isValid()) {
            for (String err : res.getErrors()) {
                System.out.println(err);
            }
        }
        double evaluation = ex.evaluate();
        if (Double.isNaN(evaluation) || Double.isInfinite(evaluation)) throw new Exception();
        return evaluation;
    }

    @FXML
    public void showJoke(ActionEvent actionEvent) throws IOException{
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("joke.fxml")));
            Stage stage = new Stage();
            stage.setTitle("I dont know what is it");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Main.primaryStage);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
