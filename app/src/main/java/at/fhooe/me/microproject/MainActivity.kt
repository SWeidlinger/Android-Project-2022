package at.fhooe.me.microproject

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.me.microproject.RecyclerView.TaskData
import at.fhooe.me.microproject.RecyclerView.MainRecyclerViewAdapter
import at.fhooe.me.microproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.view.Window
import android.widget.Button
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.Task
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore
    private lateinit var mFireStoreData: FirestoreData
    private var mSectionColor = 0
    private lateinit var mRecyclerViewData: ArrayList<TaskData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting action Bar
        binding.activityMainActionBar.showOverflowMenu()
        setSupportActionBar(binding.activityMainActionBar)

        if (mAuth.currentUser != null) {
            //getting the data from the database and populating the textView for the name of the user
            mDb.collection("users").document(mAuth.uid.toString()).get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    binding.activityMainCollapsingToolbar.title =
                        it.getResult()!!["firstName"].toString() + "'s S****"
                    mFireStoreData = FirestoreData(
                        it.getResult()!!["firstName"].toString(),
                        it.getResult()!!["lastName"].toString(),
                        it.getResult()!!["priorityA"] as ArrayList<String>,
                        it.getResult()!!["priorityB"] as ArrayList<String>,
                        it.getResult()!!["priorityC"] as ArrayList<String>,
                        it.getResult()!!["priorityD"] as ArrayList<String>
                    )
                    mSectionColor = it.getResult()!!["sectionColor"].toString().toInt()
                    //Priority A
                    val dataA = TaskData("Priority A", mFireStoreData.priorityA)
                    val dataB = TaskData("Priority B", mFireStoreData.priorityB)
                    val dataC = TaskData("Priority C", mFireStoreData.priorityC)
                    val dataD = TaskData("Priority D", mFireStoreData.priorityD)
                    mRecyclerViewData = ArrayList<TaskData>()
                    mRecyclerViewData.add(dataA)
                    mRecyclerViewData.add(dataB)
                    mRecyclerViewData.add(dataC)
                    mRecyclerViewData.add(dataD)

                    binding.activityMainRecyclerView.layoutManager =
                        LinearLayoutManager(this@MainActivity)

                    binding.activityMainRecyclerView.adapter = MainRecyclerViewAdapter(mRecyclerViewData, mSectionColor)
                    binding.activityMainAddButton.backgroundTintList = ColorStateList.valueOf(mSectionColor)

                    //add horizontal separation line
                    binding.activityMainRecyclerView.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                } else {
                    binding.activityMainCollapsingToolbar.title = "User's S****"
                    Toast.makeText(this, "Fetching Data failed!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.activityMainAddButton.setOnClickListener{
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    //check if user is already logged in
    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
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

    fun showSectionColorDialog(){
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_change_section_color)
        val previousColor = mSectionColor

        val cardView1 = dialog.findViewById(R.id.dialog_change_section_color_cardView1) as MaterialCardView
        val cardView2 = dialog.findViewById(R.id.dialog_change_section_color_cardView2) as MaterialCardView
        val cardView3 = dialog.findViewById(R.id.dialog_change_section_color_cardView3) as MaterialCardView
        val cardView4 = dialog.findViewById(R.id.dialog_change_section_color_cardView4) as MaterialCardView
        val cardView5 = dialog.findViewById(R.id.dialog_change_section_color_cardView5) as MaterialCardView
        val cardView6 = dialog.findViewById(R.id.dialog_change_section_color_cardView6) as MaterialCardView
        val saveButton = dialog.findViewById(R.id.dialog_change_section_color_button_save) as Button

        cardView1.setCardBackgroundColor(Color.BLACK)
        cardView2.setCardBackgroundColor(Color.GRAY)
        cardView3.setCardBackgroundColor(Color.BLUE)
        cardView4.setCardBackgroundColor(Color.CYAN)
        cardView5.setCardBackgroundColor(Color.GREEN)
        cardView6.setCardBackgroundColor(Color.RED)

        when (mSectionColor){
            Color.BLACK -> cardView1.isChecked = true
            Color.GRAY -> cardView2.isChecked = true
            Color.BLUE -> cardView3.isChecked = true
            Color.CYAN -> cardView4.isChecked = true
            Color.GREEN -> cardView5.isChecked = true
            Color.RED -> cardView6.isChecked = true
        }

        fun unselectAllCardViews(){
            cardView1.isChecked = false
            cardView2.isChecked = false
            cardView3.isChecked = false
            cardView4.isChecked = false
            cardView5.isChecked = false
            cardView6.isChecked = false
        }

        fun updateColor(){
            binding.activityMainRecyclerView.adapter = MainRecyclerViewAdapter(mRecyclerViewData, mSectionColor)
            binding.activityMainAddButton.backgroundTintList = ColorStateList.valueOf(mSectionColor)
        }

        cardView1.setOnClickListener{
            unselectAllCardViews()
            cardView1.isChecked = true
            mSectionColor = Color.BLACK
            updateColor()
        }

        cardView2.setOnClickListener{
            unselectAllCardViews()
            cardView2.isChecked = true
            mSectionColor = Color.GRAY
            updateColor()
        }

        cardView3.setOnClickListener{
            unselectAllCardViews()
            cardView3.isChecked = true
            mSectionColor = Color.BLUE
            updateColor()
        }

        cardView4.setOnClickListener{
            unselectAllCardViews()
            cardView4.isChecked = true
            mSectionColor = Color.CYAN
            updateColor()
        }

        cardView5.setOnClickListener{
            unselectAllCardViews()
            cardView5.isChecked = true
            mSectionColor = Color.GREEN
            updateColor()
        }

        cardView6.setOnClickListener{
            unselectAllCardViews()
            cardView6.isChecked = true
            mSectionColor = Color.RED
            updateColor()
        }

        saveButton.setOnClickListener{
            mDb.collection("users").document(mAuth.uid.toString()).update("sectionColor", mSectionColor)
            dialog.dismiss()
        }

        dialog.setOnCancelListener {
            mSectionColor = previousColor
            updateColor()
        }

        dialog.show()
    }
}