## List of deprecated functions replaced
1. Fragment Sample(from Intent Sample)
    1. Instead of using Intent, this sample program uses Fragment.
    This Allows to show activities without removing buttons.
1. Web Sample
    1. AsyncTask: from AsyncTask to [Concurrency Utilities](https://developer.android.com/topic/libraries/architecture/coroutines)
    1. Progress Dialog: from ProgressDialog to [ProgressBar](https://developer.android.com/reference/android/widget/ProgressBar) and [AlertDialog](https://developer.android.com/reference/android/app/AlertDialog)
1. Camera Sample
    1. Camera API: from Camera class to [CameraX Jetpack library](https://developer.android.com/training/camerax)
    2. View for camera: from SurfaceView to [PreviewView](https://developer.android.com/reference/kotlin/androidx/camera/view/PreviewView)
    3. Full screen: from WindowManager to [WindowInsetsController](https://developer.android.com/training/system-ui/immersive)