package br.com.agrego.sys.ui.presenter;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.agrego.sys.business.GrupoBC;
import br.com.agrego.sys.business.UsuarioBC;
import br.com.agrego.sys.domain.Grupo;
import br.com.agrego.sys.exeption.MyException;
import br.com.agrego.sys.ui.view.GrupoView;
import br.com.agrego.sys.ui.view.UsuarioView;
import br.com.agrego.sys.util.components.BotoesBasicos.EnumEstado;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.vaadin.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessClear;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessSave;
import br.gov.frameworkdemoiselle.vaadin.template.AbstractPresenter;

@ViewController
@SessionScoped
public class GrupoPresenter extends AbstractPresenter<GrupoView> {

	private static final long serialVersionUID = 1L;

	@Inject	private UsuarioBC usuarioBC;
	@Inject	private GrupoBC grupoBC;
	
	public void processSave(@Observes @ProcessSave UsuarioView view) {
		getView().usuarios.setContainerDataSource(CollectionContainer.fromBeans(usuarioBC.findAll()));
		System.out.println("teste: ");
	}
	
	public void processSave(@Observes @ProcessSave Grupo grupo) {
		try {
			grupoBC.save(grupo);
		} catch (MyException e) {
			e.showMessage(getView());
			setList(grupoBC.findAll());
			clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processItemSelection(@Observes @ProcessItemSelection GrupoView view) {
		Grupo grupo = (Grupo) view.tabela.getValue();
		if (grupo!=null){
			view.descricao.setValue(grupo.getDescricao());
			view.bb.setEstado(EnumEstado.ITEM_SELECIONADO);
		}else{
			view.bb.setEstado(EnumEstado.SEM_ITEM);
		}
	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView GrupoView view) {
		getView().usuarios.setContainerDataSource(CollectionContainer.fromBeans(usuarioBC.findAll()));
		setList(grupoBC.findAll());
	}
	
	public void processFormClear(@Observes @ProcessClear GrupoView view) {
		clear();
	}

	private void clear() {
		getView().descricao.setValue(null);
		getView().usuarios.setValue(null);
		getView().tabela.select(null);
	}

	private void setList(List<Grupo> lista){
		getView().tabela.setContainerDataSource(CollectionContainer.fromBeans(lista));
		getView().tabela.setVisibleColumns(new Object[]{"id", "descricao", "usuariosString"});
	}

}
