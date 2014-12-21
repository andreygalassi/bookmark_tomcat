package br.com.agrego.sys.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.agrego.sys.business.GrupoBC;
import br.com.agrego.sys.business.PermissaoBC;
import br.com.agrego.sys.domain.Permissao;
import br.com.agrego.sys.ui.view.PermissaoView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.vaadin.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessClear;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessDelete;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessSave;
import br.gov.frameworkdemoiselle.vaadin.template.AbstractPresenter;

@ViewController
@SessionScoped
public class PermissaoPresenter extends AbstractPresenter<PermissaoView> {

	private static final long serialVersionUID = 1L;

	@Inject	private GrupoBC grupoBC;
	@Inject	private PermissaoBC permissaoBC;
	
	public void processSave(@Observes @ProcessSave Permissao permissao) {
		if (permissao.getId() != null) {
			permissaoBC.update(permissao);
		} else {
			permissaoBC.insert(permissao);
		}
		getView().setList(permissaoBC.findPermissoes());
		getView().limpar();
	}
	
	public void processDelete(@Observes @ProcessDelete Permissao permissao) {
		if (permissao.getId() != null) {
			permissaoBC.delete(permissao.getId());
		} 
		getView().setList(permissaoBC.findPermissoes());
		getView().limpar();
	}

	public void processItemSelection(@Observes @ProcessItemSelection Permissao permissao) {

	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView PermissaoView view) {
		view.setGrupo(grupoBC.findGrupos());
		view.setList(permissaoBC.findPermissoes());
	}
	
	public void processFormClear(@Observes @ProcessClear PermissaoView view) {
		view.getTabela().select(null);
		view.limpar();
		view.setList(permissaoBC.findPermissoes());
	}

}
