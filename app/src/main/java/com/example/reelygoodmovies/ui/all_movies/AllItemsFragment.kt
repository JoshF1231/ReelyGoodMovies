    package com.example.reelygoodmovies.ui.all_movies

    import android.app.AlertDialog
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.net.Uri
    import android.os.Bundle
    import android.speech.RecognizerIntent
    import android.view.*
    import android.widget.Button
    import android.widget.SearchView
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity.RESULT_OK
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.activityViewModels
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.ItemTouchHelper
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.reelygoodmovies.R
    import com.example.reelygoodmovies.data.models.Movie
    import com.example.reelygoodmovies.databinding.AllItemsLayoutBinding
    import com.example.reelygoodmovies.ui.ActivityViewModel
    import com.example.reelygoodmovies.utils.Error
    import com.example.reelygoodmovies.utils.Loading
    import com.example.reelygoodmovies.utils.Success
    import dagger.hilt.android.AndroidEntryPoint
    import android.Manifest
    import android.provider.ContactsContract
    import androidx.core.os.bundleOf
    import androidx.fragment.app.viewModels
    import com.bumptech.glide.Glide
    import com.example.reelygoodmovies.ui.add_edit_movie.EditViewModel

    @AndroidEntryPoint
    class AllItemsFragment : Fragment() {
        private var _binding: AllItemsLayoutBinding? = null
        private val binding get() = _binding!!
        private val viewModel: ActivityViewModel by activityViewModels()
        private val editViewModel: EditViewModel by viewModels()
        private lateinit var adapter: ItemAdapter
        private var currentMovie: Movie? = null


        private val recognizeSpeechLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { it1 ->
                        viewModel.setRecognition(
                            it1
                                .joinToString(" ")
                        )
                    }
                }
            }

        private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    currentMovie?.let { showContactsDialog(it) }
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }


        private fun requestContactPermission(movie: Movie) {
            currentMovie = movie

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                currentMovie?.let { showContactsDialog(it) }
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = AllItemsLayoutBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            viewModel.initializeSearch()

            viewModel.movies.observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    is Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                    }
                    is Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recycler.visibility = View.GONE
                        Toast.makeText(requireContext(), result.status.message, Toast.LENGTH_SHORT).show()
                    }
                    is Success -> {
                        binding.progressBar.visibility = View.GONE
                        val movieList = result.status.data
                        if (movieList.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "No movies available", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.recycler.visibility = View.VISIBLE
                            adapter.updateMovies(movieList) // אם הסרטים לא ריקים, מעדכן את האדאפטר
                        }
                    }
                }
            }






            viewModel.recognition.observe(viewLifecycleOwner) { recognitionText ->
                binding.searchView.setQuery(recognitionText, false)
            }

            binding.ibMicSearch.setOnClickListener {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.mic_talk))
                }
                recognizeSpeechLauncher.launch(intent)
            }


            // Initialize Adapter
            adapter = ItemAdapter(
                emptyList(),
                object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        val movie = adapter.getItem(index)
                        viewModel.setMovie(movie)

                        findNavController().navigate(R.id.action_allItemsFragment2_to_detailedItemFragment
                        , bundleOf("id" to movie.id))
                    }

                    override fun onItemLongClicked(index: Int) {
                        val movie = adapter.getItem(index)
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                            requestContactPermission(movie) // אם אין הרשאה, בקש הרשאה
                        } else {
                            showContactsDialog(movie) // מעביר את הסרט לפונקציה
                        }
                    }

                override fun onEditButtonClick(index: Int) {
                val movie = adapter.getItem(index)
                viewModel.setMovie(movie)
                editViewModel.setEditMode(true)
                findNavController().navigate(R.id.action_allItemsFragment2_to_addOrEditItemFragment
                , bundleOf("id" to movie.id))
            }

                override fun onFavButtonClick(index: Int) {
                    val movie = adapter.getItem(index)
                    movie.favorite = !movie.favorite
                    viewModel.updateMovie(movie)
                    adapter.notifyItemChanged(index) // Ensure UI updates

                }
            })

            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())


            // Observe filtered movies
            viewModel.filteredMovies.observe(viewLifecycleOwner) { filteredMovies ->
                adapter.updateMovies(filteredMovies)
                binding.recycler.visibility = if (filteredMovies.isEmpty()) View.GONE else View.VISIBLE
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteMovieDialog(viewHolder)
                }
            }).attachToRecyclerView(binding.recycler)

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.searchView.clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    viewModel.filterMovies(newText.orEmpty())
                    return true
                }
            })
        // Add new movie button
        binding.fabAddItem.setOnClickListener {
            editViewModel.setEditMode(false)
            editViewModel.clearAllData()
            findNavController().navigate(R.id.action_allItemsFragment2_to_addOrEditItemFragment,
                bundleOf("id" to -1))
        }



        }
        // שליחת הסרט לאיש קשר שנבחר מתוך האפליקציה שלך
        private fun shareMovieWithContact(contact: String, movie: Movie) {
            val shareText = """
        Hey, check out this movie: ${movie.title}
        
        Plot: ${movie.plot}
        
        Rating: ${movie.rate}/10
    """.trimIndent()


            // יצירת Intent לשיתוף טקסט
            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$contact")  // מספר הטלפון של איש הקשר
                putExtra("sms_body", shareText)  // טקסט של הסרט שברצונך לשתף
            }

            try {
                startActivity(smsIntent)  // פותח את אפליקציית SMS עם ההודעה שהכנו
            } catch (e: Exception) {
                e.printStackTrace()
                // טיפול במידה ו-Intent נכשל (אם אין אפליקציית SMS או בעיה אחרת)
            }
        }

        // הצגת דיאלוג לבחירת איש קשר מתוך אנשי הקשר של המשתמש
        fun showContactsDialog(movie: Movie) {
            val contacts = getContacts() // קבלת רשימת אנשי קשר
            val contactNames = contacts.map { it.first as CharSequence }.toTypedArray() // המרת השמות ל-CharSequence

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose a contact to share the movie")
            builder.setItems(contactNames) { _, which ->
                val contact = contacts[which]
                shareMovieWithContact(contact.second, movie) // שליחת הסרט לאיש קשר עם מספר הטלפון
            }
            builder.show()
        }

        // פונקציה לחילוץ אנשי קשר מהמכשיר
        fun getContacts(): List<Pair<String, String>> {
            val contactList = mutableListOf<Pair<String, String>>()
            val contentResolver = requireContext().contentResolver
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // Uri של אנשי קשר
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ), // מה אנחנו רוצים להחזיר
                null, null, null
            )

            cursor?.use {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val number = it.getString(numberIndex)
                    contactList.add(name to number) // הוספת זוג של שם ומספר
                }
            }

            return contactList
        }





        private fun deleteMovieDialog(viewHolder: RecyclerView.ViewHolder) {
            val movieToDelete = adapter.getItem(viewHolder.adapterPosition)
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                adapter.notifyItemChanged(viewHolder.adapterPosition)
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
                viewModel.deleteMovie(movieToDelete, binding.searchView.query?.toString())

                Toast.makeText(requireContext(), getString(R.string.item_deletion, movieToDelete.title), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

//            movieToDelete.photo?.let {
//                dialogView.findViewById<ImageView>(R.id.iv_warning_image_movie).setImageURI(it.toUri())
//            }
            Glide.with(binding.root)
                .load(movieToDelete.photo.takeIf { !it.isNullOrEmpty() } ?: R.drawable.baseline_warning_red_24)
                .into(dialogView.findViewById(R.id.iv_warning_image_movie))
            dialog.show()
        }



        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }


    /*
    private fun deleteAllMoviesDialog() {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.setCancelable(false)

            dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
                viewModel.deleteAllMovies()
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.all_movies_deleted_confirmation),
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            dialogView.findViewById<TextView>(R.id.tv_delete_message).text =
                dialogView.context.getText(R.string.delete_all_message)
            dialog.show()
        }
    }
    */




