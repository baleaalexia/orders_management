package Start;


import Presentation.Controller;
import Presentation.MainView;

public class Start {

    public static void main(String[] args) {
        MainView view = new MainView();
        Controller controller = new Controller(view);
        view.setVisible(true);
    }
}