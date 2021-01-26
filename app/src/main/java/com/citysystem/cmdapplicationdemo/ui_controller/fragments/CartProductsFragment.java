package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.adapters.InvoiceLineAdapter;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.utils.OnBackPressed;
import com.citysystem.cmdapplicationdemo.viewmodels.InvoiceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.INVOICE_ID;
import static com.citysystem.cmdapplicationdemo.utils.Constants.INVOICE_VALIDATE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.LINE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.SELECTED_PARTNER;

public class CartProductsFragment extends BaseFragment implements InvoiceLineDialog.onDialogClickListener, OnBackPressed {

    Partner partner;
    Toolbar toolbar;
    long idInvoice;
    InvoiceLineDialog invoiceDialog;
    InvoiceViewModel invoiceViewModel;
    InvoiceLineAdapter adapter;
    RecyclerView recyclerView;
    TextView textViewTotalAmount;
    List<InvoiceLine> invoiceLines1 = new ArrayList<>();
    private Paint p = new Paint();

    public static CartProductsFragment cartProductsFragmentInstance(long idInvoice, Partner partner) {

        CartProductsFragment cartProductsFragment = new CartProductsFragment();
        Bundle args = new Bundle();
        args.putLong(INVOICE_ID, idInvoice);
        args.putParcelable(SELECTED_PARTNER, partner);
        cartProductsFragment.setArguments(args);
        cartProductsFragment.setHasOptionsMenu(true);
        return cartProductsFragment;

    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(Objects.requireNonNull(drawable).getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.cart_products_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        invoiceDialog = new InvoiceLineDialog();
        Log.i("mArguments", getArguments() + " CartProductsFragment");
        if (getArguments() != null) {
            idInvoice = getArguments().getLong(INVOICE_ID);
            partner = getArguments().getParcelable(SELECTED_PARTNER);
        }

        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        adapter = new InvoiceLineAdapter();
        textViewTotalAmount = view.findViewById(R.id.txt_total_amount_cart);
        recyclerView = view.findViewById(R.id.cart_invoice_products_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        invoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        invoiceViewModel.getInvoiceLinesByInvoiceIdLiveDataVM(idInvoice).observe(getViewLifecycleOwner(), new Observer<List<InvoiceLine>>() {
            @Override
            public void onChanged(@Nullable List<InvoiceLine> invoiceLines) {
                invoiceLines1 = invoiceLines;
                adapter.setInvoiceLines(invoiceLines);
                textViewTotalAmount.setText(String.valueOf(adapter.getTotalAmount()));
            }
        });

        adapter.setOnItemClickListener(new InvoiceLineAdapter.OnItemClickListener() {

            @Override
            public void onItemClickTwo(InvoiceLine line) {

                if (invoiceDialog.isAdded())
                    return;
                Bundle bundle = new Bundle();
                bundle.putParcelable(LINE, line);
                invoiceDialog.setArguments(bundle);
                invoiceDialog.setTargetFragment(CartProductsFragment.this, 1);
                if (getFragmentManager() != null) {
                    invoiceDialog.show(getFragmentManager(), "");
                }

            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    buildAlertMessageDeleteLine(adapter.getInvoiceLine(viewHolder.getAdapterPosition()), viewHolder);
                }

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (adapter.getItemCount() == 0) {
                    ProductsFragment productsFragment = ProductsFragment.ProductsFragmentInstance(partner, true, idInvoice);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, productsFragment).commit();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_delete_swipe);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width,
                                (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX / 3, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        Log.i("Arguments", getArguments() + "");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_invoice) {

            InvoiceValidateDialog invoiceValidateDialog = new InvoiceValidateDialog();
            Bundle bundle = new Bundle();
            bundle.putLong(INVOICE_VALIDATE, idInvoice);
            invoiceValidateDialog.setArguments(bundle);
            invoiceValidateDialog.setTargetFragment(CartProductsFragment.this, 11);
            if (getFragmentManager() != null) {
                invoiceValidateDialog.show(getFragmentManager(), "");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProductsFragment productsFragment = ProductsFragment.ProductsFragmentInstance(partner, true, idInvoice);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.container, productsFragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save_invoice_menu, menu);
    }

    private void buildAlertMessageDeleteLine(final InvoiceLine mInvoiceLine, final RecyclerView.ViewHolder viewHolder) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Supprimer cette ligne ?")
                .setCancelable(false)
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                })
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invoiceViewModel.deleteInvoiceLine(mInvoiceLine);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(InvoiceLine invoiceLine) {
        invoiceViewModel.UpdateInvoiceLineViaLine(invoiceLine);
    }
}
