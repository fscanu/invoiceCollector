import app, { Component, on } from 'apprun';

export default class headerComponent extends Component {
    state = 'header';

    view = (state) => {
        const user = state.user;
        const labels = state.labels;
        return <nav className="navbar navbar-expand-md navbar-dark bg-dark mb-4">
            {!user && <a className="nav-link" href="#">Top navbar</a>}
            {user && <a className="nav-link" href="#">Welcome {user.username}</a>}
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
                aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarCollapse">
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item active">
                        <a className="nav-link" href="#home">Home <span className="sr-only">(current)</span></a>
                    </li>
                    {!user && <li className="nav-item">
                        <a className="nav-link" href="#signin">SignIn</a>
                    </li>
                    }
                    {user && labels && <li className="nav-item">
                        <a className="nav-link" href="#labels">Your gmail labels</a>
                    </li>
                    }
                    {user && !labels && <li className="nav-item">
                        <a className="nav-link" href="#linkgmail">Link my gmail account!</a>
                    </li>
                    }
                    {!user && <li className="nav-item">
                        <a className="nav-link" href="#signup">SignUp</a>
                    </li>
                    }
                </ul>
                <form className="form-inline mt-2 mt-md-0">
                    <input className="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search" />
                    <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
                </form>
            </div>
        </nav>
    };

    update = {
        '//': state => state,
    };

    @on('/set-labels') setLabels = (state, labels) => ({ ...state, labels })
    @on('/set-user') setUser = (state, user) => ({ ...state, user })
}


