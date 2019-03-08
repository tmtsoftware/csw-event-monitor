// One time initialization for materializeCss JavaScript features

// Required for Materialize select elements
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('select');
    var instances = M.FormSelect.init(elems, {});
});

// Required for Materialize collapsible elements
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.collapsible');
    var instances = M.Collapsible.init(elems, {});
});

// Required for Materialize modal elements
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems, {
        // preventScrolling: "true"
    });
});

// Required for Materialize dropdown elements
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.dropdown-trigger');
    var instances = M.Dropdown.init(elems, {
        constrainWidth: "false"
        // hover: "true"
    });
});
