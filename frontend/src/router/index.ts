import {createRouter, createWebHistory} from 'vue-router';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';
import ActivityView from '../views/ActivityView.vue';
import OAuth2Redirect from '../components/OAuth2Redirect.vue';
import AdminDashboard from '../components/admin/AdminDashboard.vue';



const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/register',
            name: 'register',
            component: RegisterView
        },
        {
            path: '/oauth2/redirect',
            name: 'oauth2-redirect',
            component: OAuth2Redirect
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/',
            name: 'home',
            component: () => import('../views/HomeView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/activity',
            name: 'activity',
            component: ActivityView
        },
        {
            path: '/admin',
            name: 'admin-dashboard',
            component: AdminDashboard
        }
    ]
});

router.beforeEach((to, _from, next) => {
    const token = localStorage.getItem('user_token');
    const isProtected = to.meta.requiresAuth;

    if(isProtected && !token) {
        next('/login')
    } else {
        next();
    }
})

export default router;
