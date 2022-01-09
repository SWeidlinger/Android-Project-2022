package at.fhooe.me.microproject

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.me.microproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.ItemTouchHelper
import at.fhooe.me.microproject.RecyclerView.ChildRecyclerViewAdapter
import com.google.android.material.card.MaterialCardView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.RecyclerView.SwipeToDelete
import kotlinx.coroutines.*
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore
    private lateinit var mFireStoreData: FirestoreData
    private var mSectionColor = 0
    private val jokeRetriever: JokeRetriever = JokeRetriever()
    private lateinit var firstName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LoadingFragment>(R.id.activity_main_fragmentContainerView)
        }

        val sp = applicationContext.getSharedPreferences("at.fhooe.me.microproject.FirstName", Context.MODE_PRIVATE)
        firstName = sp.getString("firstName", "User")!!
        binding.activityMainCollapsingToolbar.title = "${firstName}'s S****"

        if (mAuth.currentUser != null) {
            //getting the data from the database and populating the textView for the name of the user
            mDb.collection("users").document(mAuth.uid.toString()).get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    with(getSharedPreferences("at.fhooe.me.microproject.FirstName", Context.MODE_PRIVATE).edit()){
                        putString("firstName", it.result!!["firstName"].toString())
                        apply()
                        val sp = applicationContext.getSharedPreferences("at.fhooe.me.microproject.FirstName", Context.MODE_PRIVATE)
                        firstName = sp.getString("firstName", "User")!!
                        binding.activityMainCollapsingToolbar.title = "${firstName}'s S****"
                    }
                    mFireStoreData = FirestoreData(
                        it.result!!["firstName"].toString(),
                        it.result!!["priorityA"] as ArrayList<String>,
                        it.result!!["priorityB"] as ArrayList<String>,
                        it.result!!["priorityC"] as ArrayList<String>,
                        it.result!!["priorityD"] as ArrayList<String>
                    )
                    mSectionColor = it.result!!["sectionColor"].toString().toInt()
                    initializeData()
                } else {
                    Toast.makeText(this, "Fetching Data failed!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.activityMainAddButton.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    private fun initializeData() {
        //setting action Bar
        setSupportActionBar(binding.activityMainActionBar)

        setSectionsColors()

        setupRecyclerView(
            binding.activityMainRecyclerviewPriorityA,
            mFireStoreData.priorityA,
            binding.activityMainCardViewRecyclerviewPriorityA,
            "Priority A"
        )

        setupRecyclerView(
            binding.activityMainRecyclerviewPriorityB,
            mFireStoreData.priorityB,
            binding.activityMainCardViewRecyclerviewPriorityB,
            "Priority B"
        )

        setupRecyclerView(
            binding.activityMainRecyclerviewPriorityC,
            mFireStoreData.priorityC,
            binding.activityMainCardViewRecyclerviewPriorityC,
            "Priority C"
        )

        setupRecyclerView(
            binding.activityMainRecyclerviewPriorityD,
            mFireStoreData.priorityD,
            binding.activityMainCardViewRecyclerviewPriorityD,
            "Priority D"
        )

        //add horizontal separation line
        addDivider(binding.activityMainRecyclerviewPriorityA)
        addDivider(binding.activityMainRecyclerviewPriorityB)
        addDivider(binding.activityMainRecyclerviewPriorityC)
        addDivider(binding.activityMainRecyclerviewPriorityD)

        //removing loading fragment again since the loading is finished
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.activity_main_fragmentContainerView)
        if(fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()

        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        data: ArrayList<String>,
        cardView: MaterialCardView,
        priority: String
    ) {
        recyclerView.adapter = ChildRecyclerViewAdapter(
            data,
            priority,
            cardView,
            binding.activityMainActionBar.menu,
            binding.root
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false

        //to set the cardview invisible if no item is in the list so there wont be an ugly margin
        cardView.isInvisible = data.isEmpty()

        ItemTouchHelper(SwipeToDelete((recyclerView.adapter as ChildRecyclerViewAdapter), applicationContext)).attachToRecyclerView(
            (recyclerView)
        )
    }

    //check if the cheer me up button needs to be visible
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.activity_main_menu_cheerMeUp)?.isVisible =
            mFireStoreData.priorityA.size > 2
        return super.onPrepareOptionsMenu(menu)
    }

    //populating ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //assigning Function to ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var txt: String? = null
        when (item.itemId) {
            R.id.activity_main_menu_cheerMeUp -> {
                showCheerMeUpDialog()
            }
            R.id.activity_main_menu_changePassword -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
            R.id.activity_main_menu_changeSectionColor -> {
                showSectionColorDialog()
            }
            R.id.activity_main_menu_help -> {
                txt = "HELP IM STUCK INSIDE THE PHONE!!!"
            }
            R.id.activity_main_menu_signOut -> {
                txt = "User signed out"
                mAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else -> {
                Log.e(
                    "",
                    "MainActivity::onOptionsItemsSelected ... unhandled ID ${item.itemId} encountered"
                )
            }
        }
        txt?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun fetchJoke(setupText: TextView, deliveryText: TextView, progressBar: ProgressBar) {
        val jokeFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Toast.makeText(this, "Error fetching Joke", Toast.LENGTH_LONG).show()
        }

        val scope = CoroutineScope(jokeFetchJob + Dispatchers.Main)

        scope.launch(errorHandler) {
            val joke = jokeRetriever.getJoke()

            progressBar.isVisible = false

            if (joke.type.equals("twopart")) {
                setupText.isVisible = true
                setupText.text = joke.setup
                deliveryText.text = joke.delivery
                return@launch
            }
            deliveryText.text = joke.joke
            setupText.isVisible = false
        }
    }

    private fun showCheerMeUpDialog() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_cheer_me_up)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val setupText = dialog.findViewById(R.id.dialog_cheer_me_up_textView_setup) as TextView
        val deliveryText =
            dialog.findViewById(R.id.dialog_cheer_me_up_textView_delivery) as TextView
        val btnRefresh = dialog.findViewById(R.id.dialog_cheer_me_up_button_refresh) as Button
        val btnBack = dialog.findViewById(R.id.dialog_cheer_me_up_button_back) as Button
        val progressBar = dialog.findViewById(R.id.dialog_cheer_me_up_progressBar) as ProgressBar

        fetchJoke(setupText, deliveryText, progressBar)

        var counterProcrastinating = 0
        btnRefresh.setOnClickListener {
            fetchJoke(setupText, deliveryText,progressBar)
            if (++counterProcrastinating % 7 == 0) {
                Toast.makeText(
                    this,
                    "I think that was enough cheering up for now...",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        btnBack.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    private fun setSectionsColors() {
        binding.activityMainCardViewPriorityA.setCardBackgroundColor(mSectionColor)
        binding.activityMainCardViewPriorityB.setCardBackgroundColor(mSectionColor)
        binding.activityMainCardViewPriorityC.setCardBackgroundColor(mSectionColor)
        binding.activityMainCardViewPriorityD.setCardBackgroundColor(mSectionColor)
    }

    private fun showSectionColorDialog() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_change_section_color)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val color1 = Color.BLACK
        val color2 = Color.GRAY
        val color3 = Color.parseColor("#4B7BF2")
        val color4 = Color.parseColor("#8AE043")
        val color5 = Color.parseColor("#F7CE3E")
        val color6 = Color.parseColor("#F07F4B")

        val cardView1 =
            dialog.findViewById(R.id.dialog_change_section_color_cardView1) as MaterialCardView
        val cardView2 =
            dialog.findViewById(R.id.dialog_change_section_color_cardView2) as MaterialCardView
        val cardView3 =
            dialog.findViewById(R.id.dialog_change_section_color_cardView3) as MaterialCardView
        val cardView4 =
            dialog.findViewById(R.id.dialog_change_section_color_cardView4) as MaterialCardView
        val cardView5 =
            dialog.findViewById(R.id.dialog_change_section_color_cardView5) as MaterialCardView
        val cardView6 =
            dialog.findViewById(R.id.dialog_change_section_color_cardView6) as MaterialCardView
        val backButton = dialog.findViewById(R.id.dialog_change_section_color_button_back) as Button

        cardView1.setCardBackgroundColor(color1)
        cardView2.setCardBackgroundColor(color2)
        cardView3.setCardBackgroundColor(color3)
        cardView4.setCardBackgroundColor(color4)
        cardView5.setCardBackgroundColor(color5)
        cardView6.setCardBackgroundColor(color6)

        when (mSectionColor) {
            color1 -> cardView1.isChecked = true
            color2 -> cardView2.isChecked = true
            color3 -> cardView3.isChecked = true
            color4 -> cardView4.isChecked = true
            color5 -> cardView5.isChecked = true
            color6 -> cardView6.isChecked = true
        }

        fun unselectAllCardViews() {
            cardView1.isChecked = false
            cardView2.isChecked = false
            cardView3.isChecked = false
            cardView4.isChecked = false
            cardView5.isChecked = false
            cardView6.isChecked = false
        }

        cardView1.setOnClickListener {
            unselectAllCardViews()
            cardView1.isChecked = true
            mSectionColor = color1
            setSectionsColors()
        }

        cardView2.setOnClickListener {
            unselectAllCardViews()
            cardView2.isChecked = true
            mSectionColor = color2
            setSectionsColors()
        }

        cardView3.setOnClickListener {
            unselectAllCardViews()
            cardView3.isChecked = true
            mSectionColor = color3
            setSectionsColors()
        }

        cardView4.setOnClickListener {
            unselectAllCardViews()
            cardView4.isChecked = true
            mSectionColor = color4
            setSectionsColors()
        }

        cardView5.setOnClickListener {
            unselectAllCardViews()
            cardView5.isChecked = true
            mSectionColor = color5
            setSectionsColors()
        }

        cardView6.setOnClickListener {
            unselectAllCardViews()
            cardView6.isChecked = true
            mSectionColor = color6
            setSectionsColors()
        }

        backButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.setOnCancelListener {
            mDb.collection("users").document(mAuth.uid.toString())
                .update("sectionColor", mSectionColor)
        }

        dialog.show()
    }

    //function so that no divider gets drawn behind the last element in the recyclerview
    private fun addDivider(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(
            object :
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position: Int = parent.getChildAdapterPosition(view)
                    // hide the divider for the last child
                    if (position == state.itemCount - 1) {
                        outRect.setEmpty()
                    } else {
                        super.getItemOffsets(outRect, view, parent, state)
                    }
                }
            }
        )
    }
}