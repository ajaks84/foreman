package by.dziashko.frm.ui.views.sellers;

import by.dziashko.frm.backend.entity.Seller;
import by.dziashko.frm.backend.service.SellerService;
import by.dziashko.frm.ui.forms.SellerListForm;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "sellers", layout = MainView.class)
@PageTitle("Sellers List")
@CssImport("./styles/views/sellers/sellers-list-view.css")
public class SellersListView extends VerticalLayout {

    SellerService sellerService;
    Grid<Seller> grid = new Grid<>(Seller.class);
    SellerListForm form;

    public SellersListView( SellerService sellerService) {
        this.sellerService = sellerService;
        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new SellerListForm();
        form.addListener(SellerListForm.SaveEvent.class, this::saveSeller);
        form.addListener(SellerListForm.CloseEvent.class, e -> closeEditor());
        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);

        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {

        Button addContactButton = new Button("New Seller");
        addContactButton.addClickListener(click -> addSeller());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addSeller() {
        grid.asSingleSelect().clear();
        editSeller(new Seller());
    }

    private void configureGrid() {
        grid.addClassName("seller-grid");
        grid.setSizeFull();
        grid.setColumns("name", "lastName","phoneNumber");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editSeller(event.getValue()));
    }

    private void updateList() {
        grid.setItems(sellerService.findAll());
    }

    public void editSeller(Seller seller) {
        if (seller == null) {
            closeEditor();
        } else {
            form.setSeller(seller);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setSeller(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void saveSeller(SellerListForm.SaveEvent event) {
        sellerService.save(event.getSeller());
        updateList();
        closeEditor();
    }
    private void deleteSeller(SellerListForm.DeleteEvent event) {
        sellerService.delete(event.getSeller());
        updateList();
        closeEditor();
    }

}
