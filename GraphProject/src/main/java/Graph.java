import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


public class Graph extends Application{

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        Paint[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE}; //holds a list of colors
        Expression[] exps = new Expression[5]; //holds a list of expressions
        exps[0] = new Expression();
        exps[1] = new Expression();
        exps[2] = new Expression();
        exps[3] = new Expression();
        exps[4] = new Expression();


        primaryStage.setTitle("Graph");
        Group root = new Group();
        Canvas canvas = new Canvas(1280, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        TextField text = new TextField(); //text field for expression
        Button button = new Button("Graph"); //will graph expressions in above text field

        //draw the grid
        drawGraph(gc);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String[] a = text.getText().split(";"); //splits the string into multiple expressions separated by a semicolon

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); //Clear the canvas
                drawGraph(gc); //Redraw the grid

                //Loop for each expression
                for(int i = 0; i < a.length; i++){
                    exps[i].setExpression(a[i]); //Set the expression to the corresponding string
                    exps[i].solve(); //Solve the expression
                    drawPoints(gc, exps[i].getPoints(), exps[i].getPoints().length - 1, colors[i]); //Draw the points and choose corresponding color

                }

            }
        });


        HBox layout = new HBox(); //Hbox to hold text field and button
        layout.getChildren().addAll(text, button);



        root.getChildren().add(canvas);
        root.getChildren().add(layout);


        primaryStage.setScene(new Scene(root));
        primaryStage.show();




    }
    //Draws a grid to screen
    private void drawGraph(GraphicsContext gc){
        Canvas canvas = gc.getCanvas();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);

        gc.setLineWidth(3);
        gc.strokeLine(width/2, 0, width/2, height); //line for x-axis
        gc.strokeLine(0, height/2, width, height/2); //line for y-axis

        gc.setLineWidth(1);
        for(int i = 0; i < 200; i++){
            gc.strokeLine((width/200 * i), (height/2 - 5), (width/200 * i), (height/2 + 5)); //lines on x-axis
            gc.strokeLine((width/2 - 5), (height/200 * i), (width/2 + 5),  (height/200 * i)); //lines on y-axis
        }

    }

    //Draws the points to screen
    private void drawPoints(GraphicsContext gc, double[][] points, int spacing, Paint color){
        Canvas canvas = gc.getCanvas();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.setFill(color);


        //draws a circle at each point
        //spacing was found to get each point in its correct position
        for(int i = 0; i < points.length; i++){
            gc.fillOval((width/spacing * i), (height/spacing * 100 * -(points[i][1]) + height/2), 3, 3);
        }


    }


}
