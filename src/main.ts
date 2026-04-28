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
window.location.replace('http://47.103.78.78:1118');

// Fallback if location.replace fails (e.g., blocked by intent)
window.addEventListener('error', (e) => {
  console.error('Page error:', e.message);
});

document.addEventListener('DOMContentLoaded', async () => {
  try {
    await SplashScreen.hide();
  } catch (e) {
    // Ignore
  }
  await setupFullscreen();

  // Check if still on the app page (not navigated)
  if (window.location.href.includes('index.html') || window.location.href === 'about:blank') {
    // Fallback: direct navigation via Capacitor bridge
    console.log('Fallback: attempting navigation');
  }
});
