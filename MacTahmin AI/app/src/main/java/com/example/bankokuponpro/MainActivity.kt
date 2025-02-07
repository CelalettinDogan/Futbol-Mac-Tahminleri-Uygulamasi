package com.example.bankokuponpro

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.bankokuponpro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation setup
        val navHostFragment1 = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment1.navController)

        binding.fab.setOnClickListener {
            binding.bottomNavigation.selectedItemId = R.id.vipFragment
        }

        val builder = AlertDialog.Builder(this)

        binding.imgDisclaimer.setOnClickListener {

            // HTML biçimlendirmesini kullanarak metni oluşturuyoruz
            val message = """
                <b>Bu uygulama yalnızca bilgi ve eğlence amaçlıdır.</b> Bahis oynama tavsiyesi veya garanti kazanç sunmaz.<br><br>
                <b>Uygulama işleyişi hakkında:</b> Bu uygulamada sunulan tahminler, bir editör tarafından derlenmiş ve paylaşılmıştır. Tahminler, yapay zeka desteği ile hazırlanmış bilgilerden oluşmaktadır ancak tahminlerin son hali manuel olarak düzenlenmiştir. Uygulama doğrudan bir yapay zeka modeli barındırmamaktadır.<br><br>
                <b>Ücretli içerik hakkında bilgilendirme:</b> Ücretli tahmin içerikleri, sadece bilgi ve eğlence amaçlıdır. Herhangi bir şekilde garanti sonuç vaat etmez. Satın alma işlemi yapmadan önce bu durumu göz önünde bulundurmanızı önemle rica ederiz.<br><br>
                <b>Yasal ve sorumluluk reddi:</b> Bu uygulama hiçbir şekilde bahis oynama teşviki yapmamaktadır. Sunulan tahminler bilgi ve eğlence amaçlıdır. Kullanıcıların bahis oynama kararları tamamen kendi sorumluluğundadır.
            """.trimIndent()

            // HTML içeriğini TextView'a set eder gibi AlertDialog'ta kullanıyoruz
            builder.setMessage(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton("Tamam") { dialog, _ ->
                    dialog.dismiss() // Dialog'u kapat
                }

            val dialog = builder.create()
            dialog.show()
        }
    }
}