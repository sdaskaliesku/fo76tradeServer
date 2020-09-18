<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="dark">
      <b-navbar-brand href="#">Fo76TradeHub v{{ version }}</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/" @click="$router.go()">Upload inventory dump</b-nav-item>
          <b-nav-item to="/fed76" @click="$router.go()">Upload FED76 price check enhancer
          </b-nav-item>
        </b-navbar-nav>
        <b-navbar-nav class="ml-auto">
          <b-nav-form>
            <b-button-group>
              <b-button :href="nexus" target="_blank"
                        class="my-2 my-sm-0" variant="outline-danger" type="submit">Get mod!
              </b-button>
              <b-button :href="discord" target="_blank" class="my-2 my-sm-0"
                        variant="outline-success" type="submit">Help
              </b-button>
              <b-button :href="github" target="_blank" class="my-2 my-sm-0"
                        variant="outline-primary" type="submit">Github
              </b-button>
            </b-button-group>
          </b-nav-form>

          <b-nav-item-dropdown right v-if="false">
            <template v-slot:button-content>
              <em>{{ userName }}</em>
            </template>
            <b-dropdown-item href="#" v-if="isUserLoggedIn">Profile</b-dropdown-item>
            <b-dropdown-item href="#" v-if="isUserLoggedIn">Sign Out</b-dropdown-item>
            <b-dropdown-item href="#" v-if="!isUserLoggedIn">Sign In</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import {localStorageService} from '../localStorage.service';
import {APP_VERSION, NEXUS_LINK, DISCORD_LINK, GH_LINK} from '../domain';

export default {
  name: 'NavBar',
  data() {
    let isUserLoggedIn = false;
    let userName = 'User';
    const token = localStorageService.getToken();
    if (token !== null && token !== undefined && token.length > 0) {
      isUserLoggedIn = true;
    }
    if (isUserLoggedIn) {
      userName = 'Manson';
    }

    return {
      discord: DISCORD_LINK,
      github: GH_LINK,
      nexus: NEXUS_LINK,
      version: APP_VERSION,
      isUserLoggedIn,
      userName,
    };
  },
};
</script>