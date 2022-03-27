<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="./logo.png" alt="Project logo"></a>
</p>

<h3 align="center">Alsat IPG Android Kotlin Example</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/AlsatPardakht/AlsatIPGAndroidKotlinExample.svg)](https://github.com/AlsatPardakht/AlsatIPGAndroidKotlinExample/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/AlsatPardakht/AlsatIPGAndroidKotlinExample.svg)](https://github.com/AlsatPardakht/AlsatIPGAndroidKotlinExample/pulls)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<div dir="rtl">

<p align="center">
یک مثال ساده از نحوه استفاده کردن کتابخانه <a href="https://github.com/AlsatPardakht/AlsatIPGAndroid">AlsatIPGAndroid</a> در اپلیکیشن با زبان برنامه نویسی Kotlin
    <br> 
</p>



## 📝 فهرست

- [درباره](#about)
- [نحوه استفاده](#usage)
- [ساخته شده با استفاده از](#built_using)

## 🧐 درباره <a name = "about"></a>
<p dir="rtl">
در این اپلیکیشن از کتابخانه <a href="https://github.com/AlsatPardakht/AlsatIPGAndroid">AlsatIPGAndroid</a> برای پرداخت صورت حساب به صورت اینترنتی استفاده شده است .
<br>
برای مطالعه بیشتر در مورد api مستقیم IPG آلسات پرداخت می توانید به لینک زیر مراجعه کنید :
</p>
<a href="https://www.alsatpardakht.com/TechnicalDocumentation/191">🌐 مستندات فنی IPG های مستقیم آلسات پرداخت</a><br>
همچنین مشابه این اپلیکیشن با استفاده از زبان برنامه نویسی جاوا موجود است که می توانید در لینک زیر مشاهده کنید :
<br>
<a href="https://github.com/AlsatPardakht/AlsatIPGAndroidJavaExample">نمونه استفاده از  کتابخانه AlsatIPGAndroid در Java</a>

## 🎈 نحوه استفاده <a name="usage"></a>
پس از این که مراحل <a href="https://github.com/AlsatPardakht/AlsatIPGAndroid#-%D8%B4%D8%B1%D9%88%D8%B9-%D8%A8%D9%87-%DA%A9%D8%A7%D8%B1-">شروع به کار</a> کتابخانه 
<a href="https://github.com/AlsatPardakht/AlsatIPGAndroid">AlsatIPGAndroid</a>
 را انجام دادید می توانید مراحل استفاده از کتابخانه که در ادامه آورده شده را انجام بدهید .
<br><br>
### مرحله اول : تعریف یک دیپ لینک به اکتیویتی (<a href="https://developer.android.com/training/app-links/deep-linking">deep link</a>)

ابتدا باید یک deep link برای اکتیویتی خود تعریف کنید که اکتیویتی شما بتواند نتیجه پرداخت را دریافت و برسی کند ( payment validation )
<br>برای این کار کافی است تگ intent-filter زیر را در اکتیویتی دلخواه خود کپی کنید .
<br> 
برای مشخص کردن آدرس deep link دلخواه خود کافی است در تگ data مقدار های host و scheme و pathPrefix دلخواه خود را وارد کنید .
<br>
در مثال زیر آدرس deep link برابر است با : http://www.example.com/some_path


</div>

```XML
<manifest>

    ...

    <application>
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:launchMode="singleTask">
            
            ...

            <intent-filter android:label="YOUR LABEL">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data
                    android:host="www.example.com"
                    android:pathPrefix="/some_path"
                    android:scheme="http" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

<div dir="rtl">
همچنین دقت کنید که در اکتیویتی مورد نظر حتما مقدار  اتربیوت های زیر موجود باشد که در زمان بازگشت به اپلیکیشن پس از پرداخت اینترنتی به مشکل نخورید :

</div>

```XML
android:exported="true"
android:launchMode="singleTask"
```

<div dir="rtl">
برای آشنایی بیشتر با دیپ لینک ها در اندروید می توانید داکیومنت 
<a href="https://developer.android.com/training/app-links/deep-linking">deep link</a>   را مطالعه کنید .

<br>

### مرحله دوم : ساختن نمونه از کلاس AlsatIPG

با استفاده از دستور زیر می توانید یک نمونه از کلاس AlsatIPG بسازید و با کمک این نمونه کار های پرداخت را انجام دهید :
</div>

```Kotlin
private val alsatIPG = AlsatIPG.getInstance(httpLogging = false)
```

<div dir="rtl">
مقدار false ورودی تابع getInstance به این منظور است که اطلاعات درخواست های http در  Logcat چاپ نشود (در زمان انتشار اپلیکیشن باید این مقدار false باشد ولی در هنگام debug می تواند  true باشد) 

<br>

### مرحله سوم : پیاده سازی observer ها
در این مرحله کافی است اکتیویتی یا فرگمنت  شما از LiveData های PaymentSignStatus و PaymentValidationStatus را observe کند :

</div>

```Kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ...

        observeToPaymentSignStatus()
        observeToPaymentValidationStatus()
    }

    private fun observeToPaymentSignStatus() {
        alsatIPG.paymentSignStatus.observe(this) { paymentSignResult ->
            ...
        }
    }

    private fun observeToPaymentValidationStatus() {
        alsatIPG.paymentValidationStatus.observe(this) { paymentValidationResult ->
            ...
        }
    }

}

```

<div dir="rtl">
این دو LiveData نتیجه sign شدن پرداخت و validation پرداخت را به شما بر می گرداند .
<br><br>

### مرحله چهارم : sign کردن پرداخت
برای شروع sign پرداخت کافی است با کمک تابع های signMostaghim یا signVaset موجود در کتابخانه این کار  را انجام دهید :
</div>

```Kotlin
val paymentType = PaymentType.Mostaghim
when (paymentType) {
    PaymentType.Mostaghim -> alsatIPG.signMostaghim(
        Api = API,
        Amount = 10_000,
        InvoiceNumber = "123456",
        RedirectAddress = "http://www.example.com/some_path"
    )
    PaymentType.Vaset -> alsatIPG.signVaset(
        Api = API,
        Amount = 20_000,
        RedirectAddress = "http://www.example.com/some_path",
        Tashim = emptyList(),
        InvoiceNumber = "123456"
    )
}
```

<div dir="rtl">

- مقدار paymentType می تواند با توجه به نیاز شما PaymentType.Mostaghim یا PaymentType.Vaset باشد .

- دقت کنید که مثال بالا بصورت دستی نوع پرداخت را وارد کرده  (شما می توانید با توجه به نیاز خود از یکی یا  هر دو حالت پرداخت به صورت داینامیک استفاده کنید)

- مقدار API همان کلید دریافتی شما در وب سایت آلسات پرداخت می باشد که برای دریافت آن ابتدا باید در وب سایت ثبت نام کنید و پس از مراحل احراز هویت این کلید به شما داده می شود .

- مقدار Amount همان مبلغ قالب پرداخت به ریال است . 

- مقدار InvoiceNumber همان شماره سفارش شما است .

- مقدار RedirectAddress همان آدرس دیپ لینک به اکتیویتی شماست که در فایل AndroidManifest.xml وارد کردید .

- مقدار Tashim لیست کسانی است  که مبلغ پرداختی به حساب آنها واریز خواهد شد (پرداخت بصورت واسط)

پس از فراخوانی تابع sign نتایج این فراخوانی از طریق paymentSignStatus که observe کرده بودید در دسترس است :

</div>

```Kotlin
private fun observeToPaymentSignStatus() {
    alsatIPG.paymentSignStatus.observe(this) { paymentSignResult ->
        when {
            paymentSignResult.isSuccessful -> {
                log("payment Sign Success url = ${paymentSignResult.url}")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentSignResult.url))
                startActivity(intent)
            }
            paymentSignResult.isLoading -> {
                log("payment Sign Loading ...")
            }
            else -> {
                log("payment Sign error = ${paymentSignResult.error?.message}")
            }
        }
    }
}
```

<div dir="rtl">

این observer وضعیت های موفق بودن یا لودینگ یا ارور تابع signMostaghim و یا signVaset را به شما تحویل می دهد .

همچنین می توانید از paymentSignStatusAsFlow بجای paymentSignStatus استفاده کنید که وضعیت sign را بصورت Flow به شما تحویل می دهد .
<br>
دقت کنید در زمان موفق بودن sign پرداخت شما باید با استفاده از url موجود در نتیجه یک صفحه وب برای هدایت شدن کاربر به صفحه پرداخت شاپرک باز کنید .
<br>
برای باز کردن صفحه وب می توانید از مرورگر وب یا WebView استفاده کنید که در این مثال برای سادگی از روش مرورگر وب استفاده شده است .
<br><br>

### مرحله پنجم : validation کردن پرداخت

برای این که بتوانید برای پرداخت validation انجام بدهید کافی است تابع validationMostaghim یا validationVaset را در onNewIntent اکتیویتی فراخوانی کنید :

</div>

```Kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.data?.let { data ->
        log("intent and Uri is not null")
        when (paymentType) {
            PaymentType.Mostaghim -> alsatIPG.validationMostaghim(API, data)
            PaymentType.Vaset -> alsatIPG.validationVaset(API, data)
        }
    } ?: log("intent or Uri is null")
}
```

<div dir="rtl">

- مقدار paymentType می تواند با توجه به نیاز شما PaymentType.Mostaghim یا PaymentType.Vaset باشد .

- دقت کنید که مثال بالا بصورت دستی نوع پرداخت را وارد کرده  (شما می توانید با توجه به نیاز خود از یکی یا  هر دو حالت پرداخت به صورت داینامیک استفاده کنید)

- مقدار API همان کلید دریافتی شما در وب سایت آلسات پرداخت می باشد .

- مقدار intent.data همان اطلاعاتی هستند که از سمت شاپرک به اکتیویتی شما بازگشت داده می شود و تابع validationMostaghim یا validationVaset با  استفاده از این اطلاعات معتبر بودن پرداخت را برسی می کند .

<br>
پس از آن که کاربر پرداخت را به درستی انجام داد یا به هر دلیلی موفق به پرداخت نشد شاپرک کاربر را به آدرس RedirectAddress که در زمان sign پرداخت وارد کرده بودید هدایت می کند و چون یک دیپ لینک به همین آدرس تعریف کرده اید باعث باز شدن اکتیویتی شما و فراخوانی تابع onNewIntent اکتیویتی شما و سپس فراخوانی شدن تابع validationMostaghim یا validationVaset می شود .
<br>
پس از فراخوانی تابع validation نتایج این فراخوانی از طریق paymentValidationStatus که observe کرده بودید در دسترس است :
</div>

```Kotlin
private fun observeToPaymentValidationStatus() {
    alsatIPG.paymentValidationStatus.observe(this) { paymentValidationResult ->
        when {
            paymentValidationResult.isSuccessful -> {
                log("payment Validation Success data = ${paymentValidationResult.data}")
                if (
                    (paymentValidationResult.data?.PSP?.IsSuccess == true) &&
                    (paymentValidationResult.data?.VERIFY?.IsSuccess == true)
                ) {
                    log("money transferred")
                } else {
                    log("money has not been transferred")
                }
            }
            paymentValidationResult.isLoading -> {
                log("payment Validation Loading ...")
            }
            else -> {
                log("payment Validation error = ${paymentValidationResult.error?.message}")
            }
        }
    }
}
```

<div dir="rtl">

این observer وضعیت های موفق بودن یا لودینگ یا ارور تابع validationMostaghim یا validationVaset را به شما تحویل می دهد .

همچنین می توانید از paymentValidationStatusAsFlow بجای paymentValidationStatus برای گرفتن وضعیت valiadation استفاده کنید که وضعیت را بصورت Flow به شما تحویل می دهد .
<br>
همچنین در زمانی که پرداخت موفق بوده فیلد data موجود در نتیجه اطلاعاتی در مورد پرداخت را به شما می دهد که می توانید از آن استفاده کنید .

<br>

### ⚠️ توجه 1 :
دقت کنید که موفق بودن نتیجه تابع validation به این معنی نیست که پول از حساب کاربر به حساب شما انتقال یافته ! برای کاملا مطمئن شدن از انتقال پول حتما از دستور زیر برای برسی استفاده کنید :

</div>

```Kotlin
if (
    (paymentValidationResult.data?.PSP?.IsSuccess == true) &&
    (paymentValidationResult.data?.VERIFY?.IsSuccess == true)
) {
    log("money transferred")
} else {
    log("money has not been transferred")
}
```

<div dir="rtl">

اطلاعات موجود در فیلد data شامل :
- شماره فاکتور ( PayId )
- مبلغ پرداختی به ریال ( PSP.Amount )
- تاریخ فاکتور ( PSP.InvoiceDate )
- شماره فاکتور ( PSP.InvoiceNumber )
- موفقیت پرداخت در سمت بانک ( PSP.IsSuccess )
- کد بازرگان ( PSP.MerchantCode )
- پیام سمت بانک ( PSP.Message )
- شماره مرجع ( PSP.ReferenceNumber )
- کد ترمینال ( PSP.TerminalCode )
- شماره ردیابی ( PSP.TraceNumber )
- تاریخ معامله ( PSP.TransactionDate )
- شناسه مرجع تراکنش ( PSP.TransactionReferenceID )
- شماره کارت هش شده پرداخت کننده ( PSP.TrxHashedCardNumber )
- شماره کارت ماسک پرداخت کننده ( PSP.TrxMaskedCardNumber )
- شماره کارت هش شده پرداخت کننده ( VERIFY.HashedCardNumber )
- موفقیت انجام عملیات پرداخت ( VERIFY.IsSuccess )
- شماره کارت ماسک پرداخت کننده ( VERIFY.MaskedCardNumber )
- پیام عملیات پرداخت ( VERIFY.Message )
- شماره مرجع شاپرک ( VERIFY.ShaparakRefNumber )

### ⚠️ توجه ۲ :
دراین مثال برای سادگی کار فرایند  validation سمت اپلیکیشن صورت گرفته است .
توصیه ما این است که فرایند validation  را سمت سرور خود انجام بدهید و اطلاعات سمت بانک را به اپلیکیشن نفرستید که امنیت اپلیکیشن و پرداخت شما را خیلی بالا خواهد برد .
<br>
برای این کار کافی است در زمان sign کردن پرداخت در ارگیومنت RedirectAddress آدرس وب سایت خود برای validation را وارد کنید  . در این صورت شاپرک اطلاعات validation را به آدرس وارد شده خواهد فرستاد و شما می توانید با استفاده از 
api آلسات پرداخت در وب سایت خود اعتبار پرداخت را برسی کنید و بعد برسی اعتبار پرداخت کاربر را به آدرس دیپ لینکی که در فایل AndroidManifest.xml وارد کردید redirect کنید .
<br>
در صورت استفاده از روش validation سمت سرور کاربر (هکر) نمی تواند ادعا کند پرداخت موفق  داشته (چون دسترسی به validation سمت سرور شما را ندارد) در حالی که در روش معمولی کاربر(هکر) ممکن است با ایجاد تغییراتی در اپلیکیشن شما یا با روش های دیگر موفق شود این کار را انجام دهد .

<br>
سورس کد کامل  در اکتیویتی زیر آورده شده است :
<br>

- <a href="https://github.com/AlsatPardakht/AlsatIPGAndroidKotlinExample/blob/master/app/src/main/java/com/alsatpardakht/alsatipgandroidkotlinexample/MainActivity.kt">MainActivity</a>

## ⛏️ ساخته شده با استفاده از  <a name = "built_using"></a>

</div>


- [Kotlin](https://kotlinlang.org/) - programming language
- [AlsatIPGAndroid](https://github.com/AlsatPardakht/AlsatIPGAndroid) - payment client library
- [AlsatIPGCore](https://github.com/AlsatPardakht/AlsatIPGCore) - payment core library
