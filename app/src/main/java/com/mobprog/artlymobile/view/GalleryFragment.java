package com.mobprog.artlymobile.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.adapter.ProductAdapter;
import com.mobprog.artlymobile.controller.ProductController;
import com.mobprog.artlymobile.databinding.FragmentGalleryBinding;
import com.mobprog.artlymobile.model.Product;
import com.mobprog.artlymobile.viewmodel.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ProductController productController;
    private List<Product> products = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getActivity().getApplication()).create(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        productController = new ProductController(getContext());

        final TextView tvUsername = binding.tvGalleryUsername;

        galleryViewModel.getUsername().observe(getViewLifecycleOwner(), tvUsername::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvProducts = view.findViewById(R.id.rv_gallery_products);
        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        productController.bindAllProducts(rvProducts);

        EditText etSearch = view.findViewById(R.id.et_gallery_search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(rvProducts.getAdapter() != null) {
                    ProductAdapter adapter = (ProductAdapter) rvProducts.getAdapter();
                    List<Product> products = adapter.getProducts();
                    setProducts(products);
                    adapter.filterItems(search(s.toString()));
                }
            }
        });
    }

    private void setProducts(List<Product> products) {
//      Check if products is all products, and set all products to this.products
        if(this.products.isEmpty() || this.products.size() < products.size()) {
            this.products = products;
        }
    }

    private List<Product> search(String searchText) {
        String trimmedSearchText = searchText.trim();

        if(searchText.isEmpty() || trimmedSearchText.isEmpty()) {
            return products;
        }

        List<Product> filteredProducts = new ArrayList<>();

        for(Product product : products) {
            if(product.getProductName().toLowerCase().contains(trimmedSearchText)) {
                filteredProducts.add(product);
            }
        }

        return filteredProducts;
    }
}