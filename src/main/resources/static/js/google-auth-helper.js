/**
 * Google Authentication Helper
 * 
 * This script provides helper functions for Google OAuth integration.
 * Include this in your frontend application to handle Google login.
 */

// Google Client ID from application properties
const GOOGLE_CLIENT_ID = '298726026318-mu2jnucfuh0r05d1qbnc05vmmfj88afs.apps.googleusercontent.com';

// Function to initialize Google Sign-In
function initGoogleSignIn() {
  // Load the Google Sign-In API script
  const script = document.createElement('script');
  script.src = 'https://accounts.google.com/gsi/client';
  script.async = true;
  script.defer = true;
  document.head.appendChild(script);
  
  script.onload = () => {
    // Initialize Google Sign-In
    google.accounts.id.initialize({
      client_id: GOOGLE_CLIENT_ID,
      callback: handleGoogleSignIn,
      auto_select: false,
      cancel_on_tap_outside: true
    });
    
    // Render the button
    google.accounts.id.renderButton(
      document.getElementById('google-signin-button'),
      {
        theme: 'outline',
        size: 'large',
        width: 240,
        text: 'signin_with'
      }
    );
  };
}

// Function to handle Google Sign-In callback
function handleGoogleSignIn(response) {
  // Get the ID token from the response
  const idToken = response.credential;
  
  // Send the token to the backend for verification
  sendTokenToBackend(idToken);
}

// Function to send the Google ID token to the backend
function sendTokenToBackend(idToken) {
  fetch('/api/auth/google/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ tokenId: idToken })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Google authentication failed');
    }
    return response.json();
  })
  .then(data => {
    // On successful authentication
    if (data.success && data.data.token) {
      // Store the JWT token
      localStorage.setItem('jwtToken', data.data.token);
      
      // Store user info
      localStorage.setItem('user', JSON.stringify(data.data.user));
      
      // Redirect to dashboard or home page
      window.location.href = '/dashboard';
    } else {
      console.error('Authentication failed:', data.message);
    }
  })
  .catch(error => {
    console.error('Error during Google authentication:', error);
  });
}

// Function to sign out
function signOut() {
  // Remove token and user info from localStorage
  localStorage.removeItem('jwtToken');
  localStorage.removeItem('user');
  
  // Redirect to login page
  window.location.href = '/login';
}

// Example HTML implementation:
/*
<div id="login-container">
  <h2>Login</h2>
  
  <!-- Regular login form -->
  <form id="login-form">
    <!-- ... your regular login form fields ... -->
  </form>
  
  <div class="divider">or</div>
  
  <!-- Google Sign-In button -->
  <div id="google-signin-button"></div>
</div>

<script>
  // Initialize Google Sign-In when the page loads
  document.addEventListener('DOMContentLoaded', initGoogleSignIn);
</script>
*/ 