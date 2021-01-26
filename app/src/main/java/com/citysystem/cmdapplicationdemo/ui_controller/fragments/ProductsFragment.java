package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.adapters.ProductAdapter;
import com.citysystem.cmdapplicationdemo.entities.Invoice;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.entities.Product;
import com.citysystem.cmdapplicationdemo.entities.TariffLine;
import com.citysystem.cmdapplicationdemo.entities.TariffQuantities;
import com.citysystem.cmdapplicationdemo.utils.OnBackPressed;
import com.citysystem.cmdapplicationdemo.viewmodels.InvoiceViewModel;
import com.citysystem.cmdapplicationdemo.viewmodels.ProductViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.INVOICE_ID;
import static com.citysystem.cmdapplicationdemo.utils.Constants.IS_SAME_INVOICE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.PRODUCT_DIALOG;
import static com.citysystem.cmdapplicationdemo.utils.Constants.SELECTED_PARTNER;

public class ProductsFragment extends BaseFragment implements InvoiceDialog.onDialogClickListener, OnBackPressed {

    InvoiceDialog invoiceDialog;
    MenuItem cartItem;
    TextView textViewCounter;
    FrameLayout frameLayout;

    Invoice invoice;
    List<Product> allProducts = new ArrayList<>();
    List<Product> promoProducts = new ArrayList<>();
    List<Product> favoriteProducts = new ArrayList<>();
    ProductAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    private ProductViewModel productViewModel;
    private InvoiceViewModel invoiceViewModel;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_product:
                            adapter.setProducts(allProducts);
                            break;
                        case R.id.nav_promotion:
                            adapter.setPromoProducts(promoProducts);
                            break;
                        case R.id.nav_favorite:
                            adapter.setFavoriteProducts(favoriteProducts);
                            break;
                        case R.id.nav_category:
                            Toast.makeText(getContext(), "Category", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_brand:
                            Toast.makeText(getContext(), "Marque", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            };

    public static ProductsFragment ProductsFragmentInstance(Partner currentPartner, boolean isSameInvoice, long invoiceId) {

        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_PARTNER, currentPartner);
        args.putBoolean(IS_SAME_INVOICE, isSameInvoice);
        args.putLong(INVOICE_ID, invoiceId);
        productsFragment.setArguments(args);
        productsFragment.setHasOptionsMenu(true);
        return productsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.products_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("MethodTest", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        invoiceDialog = new InvoiceDialog();
        Log.i("mArguments", getArguments() + " ProductsFragment");
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        adapter = new ProductAdapter();
        recyclerView = view.findViewById(R.id.invoice_products_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        invoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        allProducts = productViewModel.getAllProducts();
        promoProducts = productViewModel.getProductsPromo();
        favoriteProducts = productViewModel.getProductsFavorites();
        adapter.setProducts(allProducts);

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                if (invoiceDialog.isAdded())
                    return;
                if (getFragmentManager() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(PRODUCT_DIALOG, product);
                    invoiceDialog.setArguments(bundle);
                    invoiceDialog.setTargetFragment(ProductsFragment.this, 1);
                    invoiceDialog.show(getFragmentManager(), "");
                }
            }
        });
        if (getArguments() != null) {
            Log.i("getArguments", getArguments() + "");
            productViewModel.partner = getArguments().getParcelable(SELECTED_PARTNER);
            productViewModel.sameInvoice = getArguments().getBoolean(IS_SAME_INVOICE);
            productViewModel.invoiceId = getArguments().getLong(INVOICE_ID);
            String partnerName = "";
            if (productViewModel.partner != null) {
                partnerName = productViewModel.partner.getPartnerName();
                productViewModel.partner_id = productViewModel.partner.getId();
                productViewModel.partner_tariff = productViewModel.partner.getTariffId();
            }
            toolbar.setTitle(partnerName);
            if (productViewModel.invoiceId != -1) {
                invoice = invoiceViewModel.getInvoiceById(productViewModel.invoiceId);
                productViewModel.invoiceLineList = invoiceViewModel.getInvoiceLinesByInvoiceIdVM(productViewModel.invoiceId);
                invoiceViewModel.deleteInvoiceLineByInvoiceId(productViewModel.invoiceId);

            }
        }
        Log.i("invoiceLineList", productViewModel.invoiceLineList.size() + "");

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        buildAlertMessageEndInvoice();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.import_products) {
            importProductsFromCsvFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("onCreateOptionsMenu", productViewModel.invoiceLineList.size() + "");

        menu.clear();
        inflater.inflate(R.menu.product_invoice_menu, menu);
        final View menuCart = menu.findItem(R.id.cart).getActionView();
        textViewCounter = menuCart.findViewById(R.id.txt_cart_badge);
        textViewCounter.setText(String.valueOf(productViewModel.invoiceLineList.size()));
        frameLayout = menuCart.findViewById(R.id.notification_frame);
        cartItem = menu.getItem(1);
        if (productViewModel.invoiceLineList.size() <= 0) {
            cartItem.setVisible(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (InvoiceLine invoiceLine : productViewModel.invoiceLineList) {
                    invoiceViewModel.insertInvoiceLine(invoiceLine);
                }
                CartProductsFragment cartProductsFragment = CartProductsFragment.
                        cartProductsFragmentInstance(productViewModel.invoiceId, productViewModel.partner);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        cartProductsFragment).commit();

            }
        });
    }

    private void importProductsFromCsvFile() {

        InputStream is = getResources().openRawResource(R.raw.listproducts);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line;
        try {

            while ((line = reader.readLine()) != null) {
                String[] product = line.split(";");
                productViewModel.insertProducts(new Product(product[1].trim().toLowerCase(), Long.valueOf(product[2].trim().toLowerCase()),
                        Long.valueOf(product[3].trim().toLowerCase()), Boolean.valueOf(product[4].trim()), Double.valueOf(product[5].trim()),
                        product[6].trim().toLowerCase(), product[7].trim().toLowerCase(), Boolean.valueOf(product[8].trim().toLowerCase())));
            }

            buildAlertMessageSuccess();

        } catch (IOException e) {
            e.printStackTrace();
        }


        TariffLine tariffLine = new TariffLine(1, 1);
        TariffLine tariffLine1 = new TariffLine(1, 2);
        TariffLine tariffLine2 = new TariffLine(1, 3);
        TariffLine tariffLine3 = new TariffLine(1, 4);
        TariffLine tariffLine4 = new TariffLine(2, 1);
        TariffLine tariffLine5 = new TariffLine(2, 2);
        TariffLine tariffLine6 = new TariffLine(3, 1);
        TariffLine tariffLine7 = new TariffLine(3, 2);
        TariffLine tariffLine8 = new TariffLine(3, 3);
        TariffLine tariffLine9 = new TariffLine(3, 4);
        invoiceViewModel.insertTariffLine(tariffLine);
        invoiceViewModel.insertTariffLine(tariffLine1);
        invoiceViewModel.insertTariffLine(tariffLine2);
        invoiceViewModel.insertTariffLine(tariffLine3);
        invoiceViewModel.insertTariffLine(tariffLine4);
        invoiceViewModel.insertTariffLine(tariffLine5);
        invoiceViewModel.insertTariffLine(tariffLine6);
        invoiceViewModel.insertTariffLine(tariffLine7);
        invoiceViewModel.insertTariffLine(tariffLine8);
        invoiceViewModel.insertTariffLine(tariffLine9);

        TariffQuantities tariffQuantitie1 = new TariffQuantities(1, 1, 50, 30);
        TariffQuantities tariffQuantitie2 = new TariffQuantities(1, 51, 99, 25, 1, 1);

        TariffQuantities tariffQuantitie3 = new TariffQuantities(2, 1, 50, 17);
        TariffQuantities tariffQuantitie4 = new TariffQuantities(2, 51, 99, 17, 30, 1);

        TariffQuantities tariffQuantitie5 = new TariffQuantities(3, 1, 10, 70, 25, 1);
        TariffQuantities tariffQuantitie6 = new TariffQuantities(3, 11, 99, 70, 25, 2);

        TariffQuantities tariffQuantitie7 = new TariffQuantities(4, 1, 10, 75);
        TariffQuantities tariffQuantitie8 = new TariffQuantities(4, 11, 50, 70, 20, 1);
        TariffQuantities tariffQuantitie9 = new TariffQuantities(4, 51, 99, 70, 20, 3);

        TariffQuantities tariffQuantitie10 = new TariffQuantities(5, 1, 99, 20);
        TariffQuantities tariffQuantitie11 = new TariffQuantities(6, 1, 99, 10, 30, 1);
        TariffQuantities tariffQuantitie12 = new TariffQuantities(8, 1, 99, 150);
        TariffQuantities tariffQuantitie13 = new TariffQuantities(9, 1, 99, 50, 30, 1);

        invoiceViewModel.insertTariffQuantity(tariffQuantitie1);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie2);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie3);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie4);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie5);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie6);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie7);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie8);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie9);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie10);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie11);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie12);
        invoiceViewModel.insertTariffQuantity(tariffQuantitie13);


    }

    private void buildAlertMessageSuccess() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Success")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Toast.makeText(getContext(), "OK", Toast.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageEndInvoice() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Etes vous sur de vouloir supprimer cette vente ?")
                .setCancelable(false)
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (invoice != null) {
                            invoiceViewModel.deleteInvoice(invoice);
                            Toast.makeText(getContext(), "Vente Supprimer", Toast.LENGTH_LONG).show();
                        }

                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().
                                replace(((ViewGroup) Objects.requireNonNull(getView()).getParent()).getId(),
                                        new InvoicePartnerListFragment()).commit();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(long idProduct, int qte, String type, double priceUnit, boolean isPromo) {


        InvoiceLine line;
        if (productViewModel.partner_id == -1 || productViewModel.partner_tariff == -1) {
            Toast.makeText(getContext(), "No partner ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equals(getString(R.string.vente))) {
            line = createInvoiceLineTypeVente(isPromo, idProduct, qte, priceUnit, type);
        } else if (type.equals(getString(R.string.retour))) {
            line = createInvoiceLineTypeRetour(isPromo, idProduct, qte, priceUnit, type);
        } else if (type.equals(getString(R.string.echange))) {
            line = createInvoiceLineTypeEchange(isPromo, idProduct, qte, priceUnit, type);
        } else {
            Toast.makeText(getContext(), " is null", Toast.LENGTH_SHORT).show();
            return;
        }
        removeMultipleInvoiceLineFromList(line);
        if (!productViewModel.sameInvoice) {
            if (productViewModel.invoiceId == -1) {

                invoice = new Invoice();
                invoice.setPartnerId(productViewModel.partner_id);
                productViewModel.invoiceId = invoiceViewModel.insertJustInvoice(invoice);
                line.setInvoiceId(productViewModel.invoiceId);
                productViewModel.invoiceLineList.add(line);
                if (productViewModel.invoiceId != -1) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            } else {
                line.setInvoiceId(productViewModel.invoiceId);
                productViewModel.invoiceLineList.add(line);
            }
        } else {
            line.setInvoiceId(invoice.getId());
            productViewModel.invoiceLineList.add(line);
        }

        if (productViewModel.invoiceLineList.size() > 0) {
            Log.i("invoiceLineList", productViewModel.invoiceLineList.size() + "");
            textViewCounter.setText(String.valueOf(productViewModel.invoiceLineList.size()));
            cartItem.setVisible(true);
        }

    }


    private InvoiceLine createInvoiceLineTypeVente(boolean isPromo, long idProduct, int qte, double priceUnit, String type) {
        InvoiceLine line;
        if (isPromo) {
            long idTariffPromo = invoiceViewModel.getTariffPromo();
            long idTariffLine = invoiceViewModel.getTariffLineByTariffId(idTariffPromo, idProduct);
            final TariffQuantities tariffQuantitiesByLineIdPromo = invoiceViewModel.getTariffQuantitiesByLineId(qte, idTariffLine);
            if (tariffQuantitiesByLineIdPromo != null) {
                line = new InvoiceLine(idProduct, tariffQuantitiesByLineIdPromo.getPriceUnit(), qte, type, tariffQuantitiesByLineIdPromo.getGratisProductId(),
                        tariffQuantitiesByLineIdPromo.getGratisQuantity());
            } else {
                line = new InvoiceLine(idProduct, priceUnit, qte, type);
            }
        } else {
            long idTariffLine = invoiceViewModel.getTariffLineByTariffId(productViewModel.partner_tariff, idProduct);

            final TariffQuantities tariffQuantitiesByLineId = invoiceViewModel.getTariffQuantitiesByLineId(qte, idTariffLine);

            if (tariffQuantitiesByLineId != null) {
                line = new InvoiceLine(idProduct, tariffQuantitiesByLineId.getPriceUnit(), qte, type, tariffQuantitiesByLineId.getGratisProductId(),
                        tariffQuantitiesByLineId.getGratisQuantity());
            } else {
                line = new InvoiceLine(idProduct, priceUnit, qte, type);
            }
        }
        return line;
    }


    private InvoiceLine createInvoiceLineTypeRetour(boolean isPromo, long idProduct, int qte, double priceUnit, String type) {
        InvoiceLine line;
        if (isPromo) {
            long idTariffPromo = invoiceViewModel.getTariffPromo();
            long idTariffLine = invoiceViewModel.getTariffLineByTariffId(idTariffPromo, idProduct);
            final TariffQuantities tariffQuantitiesByLineId = invoiceViewModel.getTariffQuantitiesByLineId(qte, idTariffLine);
            if (tariffQuantitiesByLineId != null) {
                line = new InvoiceLine(idProduct, tariffQuantitiesByLineId.getPriceUnit(), qte, type, tariffQuantitiesByLineId.getGratisProductId(),
                        tariffQuantitiesByLineId.getGratisQuantity());
            } else {
                line = new InvoiceLine(idProduct, priceUnit, qte, type);
            }
        } else {
            long idTariffLine = invoiceViewModel.getTariffLineByTariffId(productViewModel.partner_tariff, idProduct);
            final TariffQuantities tariffQuantitiesByLineId = invoiceViewModel.getTariffQuantitiesByLineId(qte, idTariffLine);
            if (tariffQuantitiesByLineId != null) {
                line = new InvoiceLine(idProduct, tariffQuantitiesByLineId.getPriceUnit(), qte, type, tariffQuantitiesByLineId.getGratisProductId(),
                        tariffQuantitiesByLineId.getGratisQuantity());
            } else {
                line = new InvoiceLine(idProduct, priceUnit, qte, type);
            }
        }
        return line;
    }

    private InvoiceLine createInvoiceLineTypeEchange(boolean isPromo, long idProduct, int qte, double priceUnit, String type) {
        InvoiceLine line;
        if (isPromo) {
            long idTariffPromo = invoiceViewModel.getTariffPromo();
            long idTariffLine = invoiceViewModel.getTariffLineByTariffId(idTariffPromo, idProduct);
            final TariffQuantities tariffQuantitiesByLineId = invoiceViewModel.getTariffQuantitiesByLineId(qte, idTariffLine);
            if (tariffQuantitiesByLineId != null) {
                line = new InvoiceLine(idProduct, tariffQuantitiesByLineId.getPriceUnit(), qte, type, tariffQuantitiesByLineId.getGratisProductId(),
                        tariffQuantitiesByLineId.getGratisQuantity());
            } else {
                line = new InvoiceLine(idProduct, priceUnit, qte, type);
            }
        } else {
            long idTariffLine = invoiceViewModel.getTariffLineByTariffId(productViewModel.partner_tariff, idProduct);
            final TariffQuantities tariffQuantitiesByLineId = invoiceViewModel.getTariffQuantitiesByLineId(qte, idTariffLine);
            if (tariffQuantitiesByLineId != null) {
                line = new InvoiceLine(idProduct, tariffQuantitiesByLineId.getPriceUnit(), qte, type, tariffQuantitiesByLineId.getGratisProductId(),
                        tariffQuantitiesByLineId.getGratisQuantity());
            } else {
                line = new InvoiceLine(idProduct, priceUnit, qte, type);
            }
        }
        return line;
    }

    private void removeMultipleInvoiceLineFromList(InvoiceLine mLine) {

        for (InvoiceLine invoiceLine : productViewModel.invoiceLineList) {
            if (invoiceLine.getProductId() == mLine.getProductId() && invoiceLine.getType().equals(mLine.getType())) {
                productViewModel.invoiceLineList.remove(invoiceLine);
                return;
            }
        }
    }


}
