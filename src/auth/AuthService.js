import auth0 from 'auth0-js'
import axios from 'axios';
import { AUTH_CONFIG } from './auth0-variables'
import EventEmitter from 'eventemitter3'
import router from '../router'
import { store } from '../store/store';



class AuthService {

  constructor() {
    this.login = this.login.bind(this)
    this.setSession = this.setSession.bind(this)
    this.logout = this.logout.bind(this)
    this.isAuthenticated = this.isAuthenticated.bind(this)
    this.authenticated = this.isAuthenticated()
    this.authNotifier = new EventEmitter()
    this.auth0 = new auth0.WebAuth({
      domain: AUTH_CONFIG.domain,
      clientID: AUTH_CONFIG.clientId,
      redirectUri: AUTH_CONFIG.callbackUrl,
      audience: `https://${AUTH_CONFIG.domain}/userinfo`,
      responseType: 'token id_token',
      scope: 'openid'
    })
  }

  login() {
    axios.defaults.headers.post['Content-Type'] ='application/x-www-form-urlencoded';
    axios.post('https://devinventario.ecuatask.com/oauth/token', {
      client_id:  5,
      client_secret: 'kCRAX3lwQCbOlI0EG4i50ees4WEzYBkTOFmY4wuA',
      grant_type: 'password',
      username: 'admin@admin.com',
      password: 'password',
      scope: '*',
    })
        .then(response => {
          let rs = response.data;
          let authResult = {
            accessToken : rs.access_token,
            idToken:
          }

          this.access_token = response['data']['access_token'];
          console.log(rs,authResult);
          if (authResult && authResult.accessToken && authResult.idToken) {
            console.log('entro')
            this.setSession(authResult)
            router.replace('/default/dashboard/ecommerce')
          }

        })
        .catch(response => {
          if (err) {
            router.replace('/')
            console.log(err)
            alert(`Error: ${err.error}. Check the console for further details.`)
          }
          console.log(response)
        });
  }

  handleAuthentication() {
    console.log('Entroaca.....12sai')
    this.auth0.parseHash((err, authResult) => {
      if (authResult && authResult.accessToken && authResult.idToken) {
        this.setSession(authResult)
        router.replace('/default/dashboard/ecommerce')
      } else if (err) {
        router.replace('/')
        console.log(err)
        alert(`Error: ${err.error}. Check the console for further details.`)
      }
    })
  }

  setSession(authResult) {
    store.dispatch('signInUserWithAuth0', authResult)
    localStorage.setItem('isUserSigninWithAuth0', true)
    // Set the time that the access token will expire at
    let expiresAt = JSON.stringify(
      authResult.expiresIn * 1000 + new Date().getTime()
    )
    localStorage.setItem('access_token', authResult.accessToken)
    localStorage.setItem('id_token', authResult.idToken)
    localStorage.setItem('expires_in', expiresAt)
    this.authNotifier.emit('authChange', { authenticated: true })
  }

  logout() {
    store.dispatch('signOutUserFromAuth0')
    // Clear access token and ID token from local storage
    localStorage.removeItem('access_token')
    localStorage.removeItem('id_token')
    localStorage.removeItem('expires_at')
    this.userProfile = null
    this.authNotifier.emit('authChange', false)
    // navigate to the home route
    router.push('/session/login')
  }

  isAuthenticated() {
    // Check whether the current time is past the
    // access token's expiry time
    let expiresAt = JSON.parse(localStorage.getItem('expires_at'))
    return new Date().getTime() < expiresAt
  }
}

export default AuthService;