import app, {Component, on} from 'apprun';

import {auth, serializeObject} from '../api'

export default class signupComponent extends Component {
    state = 'signup';

    view = (state) => {

        return <div class="container">
            <div class="row">
                <div class="col-lg-10 col-xl-9 mx-auto">
                    <div class="card card-signin flex-row my-5">
                        <div class="card-img-left d-none d-md-flex">
                        </div>
                        <div class="card-body">
                            <h5 class="card-title text-center">Register</h5>
                            <form class="form-signin" onsubmit={e => this.run('register', e)}>
                                <div class="form-label-group">
                                    <input type="text" id="username" class="form-control" placeholder="Username" required autofocus />
                                    <label for="username">Username</label>
                                </div>

                                <div class="form-label-group">
                                    <input type="email" id="email" class="form-control" placeholder="Email address" required />
                                    <label for="email">Email address</label>
                                </div>

                                <hr />

                                <div class="form-label-group">
                                    <input type="password" id="password" class="form-control" placeholder="Password" required />
                                    <label for="password">Password</label>
                                </div>

                                <div class="form-label-group">
                                    <input type="password" id="confirmPassword" class="form-control" placeholder="Password" required />
                                    <label for="confirmPassword">Confirm password</label>
                                </div>

                                <button class="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Register</button>
                                <a class="d-block text-center mt-2 small" href="#signin">Sign In</a>
                                <hr class="my-4" />
                                <button class="btn btn-lg btn-google btn-block text-uppercase" type="submit"><i class="fab fa-google mr-2"></i> Sign up with Google</button>
                                <button class="btn btn-lg btn-facebook btn-block text-uppercase" type="submit"><i class="fab fa-facebook-f mr-2"></i> Sign up with Facebook</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    };

    @on('#/register') register = (state, messages) => ({...state, messages});



    @on('register') submitRegistration = async (state, e) => {
        try {
            e.preventDefault();
            const session = await auth.register(serializeObject(e.target));
            app.run('/set-user', session.user);
            app.run('route', '#home');
        } catch ({ errors }) {
            return { ...state, errors }
        }
    };

    update = {
        '#signup': state => state,
    }
}

