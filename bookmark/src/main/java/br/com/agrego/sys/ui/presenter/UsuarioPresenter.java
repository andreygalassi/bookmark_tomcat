package br.com.agrego.sys.ui.presenter;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.agrego.sys.acesscontrol.Credenciais;
import br.com.agrego.sys.business.GrupoBC;
import br.com.agrego.sys.business.UsuarioBC;
import br.com.agrego.sys.domain.Grupo;
import br.com.agrego.sys.domain.Usuario;
import br.com.agrego.sys.exeption.MyException;
import br.com.agrego.sys.ui.annotation.ProcessAdd;
import br.com.agrego.sys.ui.view.UsuarioView;
import br.com.agrego.sys.util.components.BotoesBasicos.EnumEstado;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.vaadin.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessClear;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessDelete;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.vaadin.event.ProcessSave;
import br.gov.frameworkdemoiselle.vaadin.template.AbstractPresenter;

@ViewController
@SessionScoped
public class UsuarioPresenter extends AbstractPresenter<UsuarioView> {

	private static final long serialVersionUID = 1L;
	
	@Inject	private UsuarioBC usuarioBC;
	@Inject	private GrupoBC grupoBC;
	@Inject private Credenciais credenciais;
	
	@SuppressWarnings({ "unchecked" })
//	@RequiredRole(value=EnumMenu.USUARIO.getNome())
//	@RequiredPermission(resource = "USUARIO", operation = "ALTERAR")
	public void processSave(@Observes @ProcessSave UsuarioView view) {
		Usuario usuario = null;
		try {
			credenciais.autorizacao(view, Credenciais.ACAO_ALTERAR);
			usuario = (Usuario) view.tabela.getValue();
			if (usuario==null) usuario = new Usuario();
			usuario.setLogin((String)view.login.getValue());
			usuario.setSenha((String)view.senha.getValue());
			usuario.setEmail((String)view.email.getValue());
			usuario.setGrupos((Set<Grupo>) getView().grupos.getValue());
			usuarioBC.save(usuario);
		} catch (MyException e) {
			e.showMessage(getView());
			setList(usuarioBC.findAll());
//			Beans.getBeanManager().fireEvent(usuario, new AnnotationLiteral<ProcessSave>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
//	@Transactional
//	@RequiredRole(value="CRIAR")
//	@RequiredPermission(resource = "USUARIO", operation = "CRIAR")
	public void processAdd(@Observes @ProcessAdd UsuarioView view) {
		try {
			credenciais.autorizacao(view, Credenciais.ACAO_CRIAR);
			Usuario usuario = new Usuario();
			usuario.setLogin((String)view.login.getValue());
			usuario.setSenha((String)view.senha.getValue());
			usuario.setEmail((String)view.email.getValue());
			usuario.setGrupos((Set<Grupo>) getView().grupos.getValue());
//			usuario.getGrupos().addAll((Set<Grupo>) getView().grupos.getValue());
			usuarioBC.save(usuario);
		} catch (MyException e) {
			e.showMessage(getView());
			setList(usuarioBC.findAll());
		} catch (Exception e) {
			getView().getWindow().showNotification("ERRO AO EXECUTAR AÇÃO", 3);
			e.printStackTrace();
		}
	}
//	@RequiredPermission(resource = "USUARIO", operation = "EXCLUIR")
//	@RequiredRole(value="EXCLUIR")
	public void processDelete(@Observes @ProcessDelete UsuarioView view) {
		try {
			credenciais.autorizacao(view, Credenciais.ACAO_EXCLUIR);
			Usuario usuario = (Usuario) view.tabela.getValue();
			if (usuario==null) new MyException(2, "SELECIONE UM REGISTRO PARA EXCLUIR.");
			usuario.setAtivo(false);
			usuarioBC.save(usuario);
		} catch (MyException e) {
			e.showMessage(getView());
			setList(usuarioBC.findAll());
			clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processItemSelection(@Observes @ProcessItemSelection UsuarioView view) {
//		getView().grupos.setValue(usuario.getGrupos());
		Usuario usuario = (Usuario) view.tabela.getValue();
		if (usuario!=null){
			view.login.setValue(usuario.getLogin());
			view.senha.setValue(usuario.getSenha());
			view.email.setValue(usuario.getEmail());
			view.grupos.setValue(usuario.getGrupos());
			view.bb.setEstado(EnumEstado.ITEM_SELECIONADO);
		}else{
			view.bb.setEstado(EnumEstado.SEM_ITEM);
		}
	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView UsuarioView view) {
//		view.setPerfis(perfilBC.findAll());
//		view.getCrudForm().getField("pessoa").setContainerDataSource(CollectionContainer.fromBeans(list));
		view.grupos.setContainerDataSource(CollectionContainer.fromBeans(grupoBC.findAll()));
		setList(usuarioBC.findAll());

	}
	
	public void processFormClear(@Observes @ProcessClear UsuarioView view) {
		clear();
	}

	private void clear() {
		getView().login.setValue(null);
		getView().senha.setValue(null);
		getView().email.setValue(null);
		getView().grupos.setValue(null);
		getView().tabela.select(null);
	}

	private void setList(List<Usuario> lista){
		getView().tabela.setContainerDataSource(CollectionContainer.fromBeans(lista));
		getView().tabela.setVisibleColumns(new Object[]{"id","ativo", "login", "dtCriacao", "dtUltimoAcesso", "email","gruposString"});
		getView().tabela.setColumnHeader("dtCriacao","CRIAÇÃO");
		getView().tabela.setColumnHeader("dtUltimoAcesso","ULTIMO ACESSO");
		getView().tabela.setColumnHeader("gruposString","GRUPOS");
	}
	
	
	
}
