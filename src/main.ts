import { StatusBar, Style } from '@capacitor/status-bar';
import { SplashScreen } from '@capacitor/splash-screen';

// Hide status bar and navigation bar for fullscreen
async function setupFullscreen() {
  try {
    await StatusBar.setOverlaysWebView({ overlay: true });
    await StatusBar.hide();
    await StatusBar.setStyle({ style: Style.Dark });
  } catch (e) {
    // Not running in Capacitor (browser dev)
  }
}

// Redirect to CES service
window.location.href = 'http://47.103.78.78:1118';

// Hide splash screen when ready
document.addEventListener('DOMContentLoaded', async () => {
  try {
    await SplashScreen.hide();
  } catch (e) {
    // Ignore
  }
  await setupFullscreen();
});
