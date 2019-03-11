// One time initialization for materializeCss JavaScript features
document.addEventListener('DOMContentLoaded', function() {
    M.AutoInit();

    // M.FormSelect.init(document.querySelectorAll('select'), {});
    // M.Collapsible.init(document.querySelectorAll('.collapsible'), {});
    // M.Modal.init(document.querySelectorAll('.modal'), {
    //     // preventScrolling: "true"
    // });
    // M.Dropdown.init(document.querySelectorAll('.dropdown-trigger'), {
    //     constrainWidth: "false"
    //     // hover: "true"
    // });
    // M.Tooltip.init(document.querySelectorAll('.tooltipped'), {});
});

// // Fix select scroll wheel issue: See https://github.com/Dogfalo/materialize/issues/6236
// $(document).ready(function () {
//     $('body').append('<select class="browser-default" style="position:absolute;visibility:hidden"></select>'); //this is the hack
//     M.AutoInit();
// });


