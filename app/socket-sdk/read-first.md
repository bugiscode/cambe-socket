Berikut adalah dokumentasi untuk kelas `SocketHandler` yang ditulis dalam Bahasa Indonesia. Dokumentasi ini menjelaskan fungsionalitas, metode, dan properti dari kelas tersebut.

---

## Dokumentasi Kelas `SocketHandler`

### Deskripsi

`SocketHandler` adalah objek singleton yang mengelola koneksi soket ke server menggunakan library `socket.io`. Kelas ini menangani pembuatan koneksi, pengaturan event listener, dan komunikasi dengan server melalui soket. Kelas ini juga berfungsi sebagai penghubung antara event soket dan presenter yang mengimplementasikan antarmuka `SocketPresenter`.

### Properti

- **`private lateinit var mSocket: Socket`**
    - Menyimpan referensi ke objek `Socket` yang mewakili koneksi soket ke server.

- **`private var socketPresenter: SocketPresenter? = null`**
    - Menyimpan referensi ke presenter yang mengimplementasikan antarmuka `SocketPresenter`, untuk menangani event yang diterima dari soket.

### Metode

#### `fun setSocket(serverUrl: String, path: String = "/socket.io")`

- **Deskripsi**: Menginisialisasi koneksi soket dengan server. Metode ini juga mengatur event listener untuk berbagai event soket.
- **Parameter**:
    - `serverUrl`: URL server soket.
    - `path`: Jalur untuk soket, default adalah `"/socket.io"`.
- **Catatan**: Metode ini akan menangani kesalahan seperti `URISyntaxException` dan kesalahan umum lainnya dengan melempar `RuntimeException`.

#### `private fun setupEventListeners()`

- **Deskripsi**: Mengatur listener untuk berbagai event soket dan meneruskan event tersebut ke `socketPresenter` jika tersedia.
- **Catatan**: Event yang didengarkan meliputi koneksi, pemutusan, pesan obrolan, percakapan, dan media.

#### `private fun createOkHttpClient(): OkHttpClient`

- **Deskripsi**: Membuat dan mengonfigurasi instansi `OkHttpClient` yang digunakan untuk transportasi soket.
- **Return**: Mengembalikan objek `OkHttpClient` yang sudah dikonfigurasi dengan logging dan timeout.

#### `fun getSocket(): Socket`

- **Deskripsi**: Mengembalikan instansi soket yang saat ini terhubung.
- **Return**: Objek `Socket`.

#### `fun establishConnection()`

- **Deskripsi**: Membuka koneksi soket jika belum terhubung.
- **Catatan**: Metode ini memastikan bahwa koneksi soket hanya dibuka jika belum ada koneksi aktif.

#### `fun closeConnection()`

- **Deskripsi**: Menutup koneksi soket dan menghapus semua listener.
- **Catatan**: Metode ini memastikan bahwa koneksi soket ditutup dengan benar dan tidak ada listener yang tertinggal.

#### `fun sendMessage(event: String, data: JSONObject)`

- **Deskripsi**: Mengirim pesan melalui soket dengan event tertentu dan data terkait.
- **Parameter**:
    - `event`: Nama event untuk pesan yang akan dikirim.
    - `data`: Data yang akan dikirim dalam bentuk `JSONObject`.

#### `fun setSocketPresenter(presenter: SocketPresenter)`

- **Deskripsi**: Menetapkan presenter yang akan menangani event yang diterima dari soket.
- **Parameter**:
    - `presenter`: Objek yang mengimplementasikan antarmuka `SocketPresenter`.

### Penggunaan

- **Inisialisasi Koneksi**: Panggil `setSocket` dengan URL server soket dan path untuk memulai koneksi.
- **Membuka Koneksi**: Gunakan `establishConnection` untuk membuka koneksi jika belum terhubung.
- **Menutup Koneksi**: Gunakan `closeConnection` untuk menutup koneksi dan membersihkan listener.
- **Mengirim Pesan**: Gunakan `sendMessage` untuk mengirim pesan ke server dengan event dan data yang sesuai.
- **Menetapkan Presenter**: Gunakan `setSocketPresenter` untuk menetapkan presenter yang akan menangani berbagai event soket.

### Catatan

- Pastikan `socketPresenter` diatur dengan benar sebelum memulai koneksi soket untuk menangani event dengan tepat.
- Kelas ini menangani berbagai event soket dengan logging untuk memudahkan debugging dan pelacakan masalah.

---

Dokumentasi ini memberikan gambaran umum tentang cara kerja dan penggunaan `SocketHandler`, serta menjelaskan bagaimana metode dan properti kelas ini berfungsi.