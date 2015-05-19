package org.jugvale.transfgov.cliente.paineis;

import org.jugvale.transfgov.cliente.Carregando;
import org.jugvale.transfgov.cliente.model.Agregacao;
import org.jugvale.transfgov.cliente.service.AgregacaoClientService;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AbaPorEstado extends Tab {

	ComboBox<Integer> cmbAno = new ComboBox<>();
	ComboBox<Integer> cmbMes = new ComboBox<>();
	ComboBox<String> cmbSiglas = new ComboBox<>();
	Button btnCarregarEstados = new Button("Carregar");
	AgregacaoClientService service;

	public AbaPorEstado(AgregacaoClientService service) {
		this.service = service;
		BorderPane conteudo = new BorderPane();
		HBox pnlTop = new HBox(20, cmbAno, cmbMes, cmbSiglas);
		pnlTop.setAlignment(Pos.CENTER);
		btnCarregarEstados.setOnAction(this::carregaAgregacaoPorEstado);
		conteudo.setTop(pnlTop);
		setText("Transferências por Estado");
	}

	private void carregaAgregacaoPorEstado(ActionEvent evt) {
		int ano = cmbAno.getSelectionModel().getSelectedItem();
		int mes = cmbAno.getSelectionModel().getSelectedItem();
		String sigla = cmbSiglas.getSelectionModel().getSelectedItem();
		Task<Agregacao> tarefaCargaPg = new Task<Agregacao>() {
			@Override
			protected Agregacao call() throws Exception {
				Carregando.carregando(true);
				return service.agregacaoPorAnoMesEstado(ano, mes, sigla);
			}

			@Override
			protected void succeeded() {
				Carregando.carregando(false);
				super.succeeded();
			}

			@Override
			protected void failed() {
				Carregando.carregando(true);
				super.failed();
			}
			
		};
	}

}
