package com.ftechiz.githubuserapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.ftechiz.githubuserapp.adapters.UserListAdapter
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.utils.App
import com.ftechiz.githubuserapp.utils.NetworkUtils
import com.ftechiz.githubuserapp.viewmodel.MainActivityViewModel
import com.ftechiz.githubuserapp.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener {
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var adapter: UserListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    private var isSearching: Boolean = false
    private var usersList: ArrayList<UserModel> = arrayListOf()
    private var filterList: ArrayList<UserModel> = arrayListOf()
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var context: Context
    private lateinit var noInternetLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        noInternetLayout = findViewById(R.id.no_internet_lyt)
        initRecyclerView()
        initViewModel()
        addScrollerListener()
    }

    private fun initRecyclerView() {
        mainRecyclerView = findViewById(R.id.mainRecyclerView)
        shimmerFrameLayout = findViewById(R.id.shimmerLayout)
        layoutManager = LinearLayoutManager(this)
        mainRecyclerView.layoutManager = this.layoutManager
        mainRecyclerView.hasFixedSize()
        shimmerFrameLayout.startShimmerAnimation()
        adapter = UserListAdapter(this, usersList)
        mainRecyclerView.adapter = this.adapter
        context = this
    }

    // for pagination in recyclerview
    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == usersList.size - 1) {
                        //loadMore
                        loadMoreData()
                    }
                }
            }
        })
    }

    private fun loadMoreData() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            synchronized(this) {
                if (!isLoading && !isSearching) {

                    //Adds a demo item in list for progress -> With View Type "progress"
                    usersList.add(
                        UserModel(
                            456780, "s", "s", "s", "s",
                            "s", "s", "s", "s", "s",
                            "s", "s", "s", false,
                            "s", "s", "s", "s", "s",
                            "progress"
                        )
                    )
                    adapter.notifyDataSetChanged()
                    val handler = Handler()
                    handler.postDelayed({
                        viewModel.loadData(getPage(), false)
                    }, 1500)
                    isLoading = true
                }
            }
        }
    }

    //dynamic page no. from shared pref
    private fun getPage(): Int {
        return App.getInteger(context, App.PAGE_NO)
    }

    private fun initViewModel() {
        val repo = (application as App).userRepository
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repo, getPage(), true)
        )[MainActivityViewModel::class.java]


        viewModel.users.observe(this, {
            if (it != null) {
                if (isLoading) {
                    usersList.removeAt(usersList.size - 1)
                }
                this.usersList.addAll(it)
                isLoading = false
                adapter.notifyDataSetChanged()
                shimmerFrameLayout.stopShimmerAnimation()
                shimmerFrameLayout.visibility = View.GONE

            } else {
                shimmerFrameLayout.stopShimmerAnimation()
                shimmerFrameLayout.visibility = View.GONE
                Toast.makeText(this, "List is empty!", Toast.LENGTH_SHORT).show();
            }
        })

        NetworkUtils(context)
            .observe(this, Observer { isConnected ->
                if (isConnected) {
                    // Internet Available
                    if (noInternetLayout.visibility == View.VISIBLE) {
                        noInternetLayout.visibility = View.GONE
                    }
                    loadMoreData()
                    return@Observer
                } else {
                    if (noInternetLayout.visibility == View.GONE) {
                        noInternetLayout.visibility = View.VISIBLE
                    }
                    Toast.makeText(context, "You are Offline", Toast.LENGTH_SHORT).show()
                }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.menu_search)

        val searchView = search?.actionView as SearchView
        val searchAutoComplete =
            searchView.findViewById<View>(R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.setBackgroundColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
            isSearching = true
        } else if (query == "") {
            adapter.setList(usersList)
            isSearching = false
            adapter.notifyDataSetChanged()
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)
            isSearching = true
        } else if (newText == "") {
            adapter.setList(usersList)
            isSearching = false
            adapter.notifyDataSetChanged()
        }
        return false
    }

    //search from local database on the base of username and note
    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(this, {
            filterList = it as ArrayList<UserModel>
            adapter.setList(filterList)
            adapter.notifyDataSetChanged()
        })
    }

}