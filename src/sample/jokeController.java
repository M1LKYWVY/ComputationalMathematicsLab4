package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Random;

public class jokeController {
    @FXML
    public Label jokeField;
    ArrayList<String> jokeList = new ArrayList<>();

    @FXML
    public void initialize() {
        jokeList.add("Сын спрашивает у отца-бизнесмена: \n— Пап, у нас есть что-нибудь почитать из фантастики или мистики? \n— Да, налоговый отчет моего бухгалтера.");
        jokeList.add("Одесса. Семья рыбачит. Сын: \n— Папа, а почему твой поплавок стоит, а у дедушки лежит? Голос бабушки за спиной: \n— Ой, Сема, когда у дедушки стоял... Шо он только не ловил!");
        jokeList.add("Британские ученые доказали, что один человек, \nзанятый полезным трудом, помогает найти занятие еще как минимум трем другим людям, \nодин из которых наблюдает за ним, второй – мешает, ну а третий – руководит!");
        jokeList.add("— Добрый день! Я хотел бы купить бейсбольную биту.\n — Людей бить будете? \n— Нет, в бейсбол играть, Мытищи же столица бейсбола.");
        jokeList.add("Идет по лесу медведь, видит горящую машину. \nСел в нее и сгорел.");
        Random rand = new Random();
        jokeField.setText(jokeList.get(rand.nextInt(jokeList.size())));
    }

    @FXML
    public void nextJoke(ActionEvent actionEvent) {
        Random rand = new Random();
        jokeField.setText(jokeList.get(rand.nextInt(jokeList.size())));
    }
}
