package com.example.reelygoodmovies.ui.trailer_movie

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.TrailerLayoutBinding
import com.example.reelygoodmovies.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.net.URISyntaxException

@AndroidEntryPoint
class TrailerFragment : Fragment() {
    private var _binding: TrailerLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModelTrailer: TrailerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrailerLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt("id")?.let {
            viewModelTrailer.setId(it)
        }

        viewModelTrailer.trailer.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                is Loading -> binding.progressBar.visibility = View.VISIBLE

                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    val trailerUrl = resource.status.data
                    if (trailerUrl?.isNotEmpty() == true) {
                        // אם ה-URL של הטריילר לא ריק, מציגים את הדיאלוג
                        showTrailerDialog(trailerUrl)
                    } else {
                        Toast.makeText(requireContext(), "Trailer not available", Toast.LENGTH_SHORT).show()
                    }
                }
                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = ErrorMessages.getErrorMessage(requireContext(), resource.status.message)
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showTrailerDialog(url: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("האם ברצונך לצפות בטריילר בתוך האפליקציה או לפתוח אותו ב-YouTube?")
            .setPositiveButton("הישאר באפליקציה") { dialog, _ ->
                loadTrailer(url)
                dialog.dismiss()
            }
            .setNegativeButton("פתוח ב-YouTube") { dialog, _ ->
                openYouTube(url)
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun loadTrailer(url: String) {
        binding.wwTrailer.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    if (url.startsWith("intent://")) {
                        try {
                            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                            val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                            if (intent.resolveActivity(requireContext().packageManager) != null) {
                                startActivity(intent)
                                return true
                            } else if (!fallbackUrl.isNullOrEmpty()) {
                                view.loadUrl(fallbackUrl)
                                return true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), "שגיאה בפתיחת הסרטון", Toast.LENGTH_SHORT).show()
                            return true
                        }
                    }
                    return false
                }

                // טיפול בשגיאות כלליות בטעינה
                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    super.onReceivedError(view, request, error)
                    // מונע טעינת הדף הלא זמין
                    Toast.makeText(requireContext(), "לא ניתן לטעון את הדף", Toast.LENGTH_SHORT).show()
                    // לא טוענים את ה-URL ב-WebView
                    view.stopLoading()
                }
            }
            loadUrl(url)
        }
    }




    private fun openYouTube(url: String) {
        try {
            val videoId = Uri.parse(url).getQueryParameter("v")
            if (videoId != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$videoId"))
                intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.google.android.youtube"))

                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            } else {
                Toast.makeText(requireContext(), "לא ניתן לשלוף מזהה סרטון מה-URL", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "שגיאה בפתיחת הסרטון: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
