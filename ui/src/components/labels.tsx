import app, { Component, on } from 'apprun';

export default class labelsComponent extends Component {
  state = 'labels';

  view = (state) => {
    
    if (!state.labels) return;
    return <div>
      <ul>
        {state.labels.map(label => <li>{label}</li>)}
      </ul>
    </div>
  }

  @on('/set-labels') setLabels = (state, labels) => ({ ...state, labels })
  update = {
    '#labels': state => state,
  }
}