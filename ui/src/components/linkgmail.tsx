import app, { Component, on } from 'apprun';

import {auth, serializeObject} from '../api'

export default class linkgmailComponent extends Component {
  state = 'linkgmail';

  view = (state) => {
    return <div class="container">
      <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
          <div class="card card-signin flex-row my-5">
            <div class="card-body">
              <h5 class="card-title text-center">Gmail</h5>
              <form class="form-signin" onsubmit={e => this.run('linkgmail', e)}>


                <div class="form-label-group">
                  <input type="email" id="email" class="form-control" placeholder="Email address" required />
                  <label for="email">Email address</label>
                  <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  }

  @on('linkgmail') linkgmailaccount = async (state, e) => {
    try {
        e.preventDefault();
        const session = await auth.linkgmail(serializeObject(e.target));

        app.run('route', '#home');
    } catch ({ errors }) {
        return { ...state, errors }
    }
};
  update = {
    '#linkgmail': state => state,
  }
}

