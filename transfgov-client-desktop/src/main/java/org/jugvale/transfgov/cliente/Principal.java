package org.jugvale.transfgov.cliente;

import org.jugvale.transfgov.cliente.paineis.AbaPorEstado;
import org.jugvale.transfgov.cliente.service.AgregacaoClientService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Principal extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		ProgressIndicator indicadorProgresso = new ProgressIndicator();
		AgregacaoClientService service = new AgregacaoClientService();
		TabPane painelAbas = new TabPane(new AbaPorEstado(service));
		indicadorProgresso.visibleProperty().bind(Carregando.prop());
		stage.setScene(new Scene(new StackPane(indicadorProgresso, painelAbas)));
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
