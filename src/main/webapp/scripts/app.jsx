void function() { 'use strict'

var {DefaultRoute, Route, RouteHandler} = ReactRouter

var NavItem = createActiveRouteComponent('li')

var ActivePara = createActiveRouteComponent('p', {link: false})

var App = React.createClass({
  render() {
    return <div className="App container">
      <nav className="navbar navbar-default" role="navigation">
        <div className="container">
          <span className="navbar-brand">Дикий залёт</span>
          <ul className="nav navbar-nav">
            <NavItem to="dashboard">Главная</NavItem>
            <NavItem to="tasks">Почта</NavItem>
            <li>
              <p className="navbar-text"><span className="glyphicon glyphicon-cog"></span>Вся мощь Wildfly</p>
            </li>
          </ul>
        </div>
      </nav>
      <RouteHandler/>
      <hr/>
      <footer>
        Подвал
      </footer>
    </div>
  }
})

var Dashboard = React.createClass({
  render() {
    return <div className="Dashboard">
      <h2>Dashboard</h2>
      <p>I'm a regular paragraph.</p>
      <ActivePara to="dashboard" activeClassName="special">
        I get highlighted because the Dashboard route is active.
      </ActivePara>
    </div>
  }
})

var Tasks = React.createClass({
  render() {
    return <div className="Tasks row">
      <div className="col-md-3">
        <ul className="nav nav-pills nav-stacked">
          <NavItem to="e-mail-react">Почта React</NavItem>
          <li><a href="mail.jsf">Послать почту jsf</a></li>
          <NavItem to="hibernate">Hibernate</NavItem>
        </ul>
      </div>
      <div className="col-md-9">
        <ActivePara to="hibernate" activeClassName="special">
          I get highlighted when the My Tasks route is active.
        </ActivePara>
        <RouteHandler/>
      </div>
    </div>
  }
})

var TasksDashboard = React.createClass({
  render() {
    return <div className="TasksDashboard">
      <h2>Tasks Dashboard</h2>
    </div>
  }
})

var EmailForm = React.createClass({
  render: function() {
    return (
        <form className="emailForm" onSubmit={this.onSubmit}>
          <input type="text" placeholder="to" ref="toRef" /><br/>
          <input type="text" placeholder="from" ref="fromRef"/><br/>
          <input type="text" placeholder="subject" ref="subjectRef" /><br/>
          <textarea placeholder="Say something..." ref="bodyRef" /><br/>
          <input type="submit" value="Post" />
        </form>
    );
  },

  onSubmit: function(e) {
    e.preventDefault()

    // check if form is valid
    var validation = this.refs.form.value().validation
    if (ReactForms.validation.isFailure(validation)) {
      console.log('invalid form')
      return
    }

    alert('form submitted!')
  }
});

var MyTasks = React.createClass({
  render() {
    return <div className="MyTasks">
      <h2>Hibernate</h2>
    </div>
  }
})

var routes = <Route handler={App}>
  <DefaultRoute name="dashboard" handler={Dashboard}/>
  <Route name="tasks" handler={Tasks}>
    <DefaultRoute handler={TasksDashboard}/>
    <Route name="e-mail-react" path="e-mail-react" handler={EmailForm}/>
    <Route name="hibernate" path="hibernate" handler={MyTasks}/>
  </Route>
</Route>

ReactRouter.run(routes, function(Handler) {
  React.render(<Handler/>, document.getElementById('content'))
})

}()
