(function () {
  "use strict";

  var root = this,
      $ = root.jQuery,
      GOVUK = root.GOVUK;

  $('#full-example').hide();
  $('#presentation-example').hide();
  $("#example-query-title").hide();

  $('header.no-back-link').each(function (idx, elm) {
    new GOVUK.registerToVote.BackButton(elm);
  });
  $('.optional-section, .optional-section-binary').each(function (idx, elm) {
    var toggleClass = 'optional-section',
        $elm = $(elm);
    if ($elm.data('controlText') !== undefined) {
      new GOVUK.registerToVote.OptionalControl(elm, toggleClass);
    } else {
      if ($elm.data('condition') !== undefined) {
        new GOVUK.registerToVote.ConditionalControl(elm, toggleClass);
      } else {
        new GOVUK.registerToVote.OptionalInformation(elm, toggleClass);
      }
    }
  });
  $('.selectable').each(function (idx, elm) {
    var $label = $(elm),
        $control = $label.find('input[type=radio], input[type=checkbox]'),
        controlName = $control.attr('name');

    if ($control.attr('type') === 'radio') {
      // set up event monitoring for radios sharing that name
      GOVUK.registerToVote.monitorRadios($control[0]);
    }
    new GOVUK.registerToVote.MarkSelected(elm);
    $control.on('focus', function () {
      $(this).parent('label').addClass('selectable-focus');
    });
    $control.on('blur', function () {
      $(this).parent('label').removeClass('selectable-focus');
    });
  });
  $('.country-autocomplete').each(function (idx, elm) {
    GOVUK.registerToVote.autocompletes.add($(elm));
  });

  // Custom events

  var dataType = $('input:radio[name=dataType]:checked').val();
  var queryType = $('input:radio[name=queryType]:checked').val();

  $("input:radio[name=dataType]").click(function() {
    dataType = $(this).val();
  });

  $("input:radio[name=queryType]").click(function() {
    queryType = $(this).val();
  });

  $('#full-example-button').on("click", function(e) {
    $('#full-example').toggle();
    $('#presentation-example').hide();
  });

  $('#close-full-example').on("click", function(e) {
    $('#full-example').hide();
  });

  $('#presentation-example-button').on("click", function(e) {
    $('#presentation-example').toggle();
    $('#full-example').hide();
  });

  $('#close-presentation-example').on("click", function(e) {
    $('#presentation-example').hide();
  });

    $('#submit').on("click", function(e) {
         e.preventDefault();

        function printSuccess(obj) {
            var html = '<hr/>';
            html += "<h2>This is your credential for the locate API</h2>"
            html += '<p>Note this down now as there is no way to access it again!</p>';
            html += "<p class='credential'><em>" + obj.token + "</em></p>";
            html += '<p>Try your token on the <a href="/locate/explorer">API explorer.</a>';
            return html;
        }

        function printError(errors) {
            var errorHtml = '<hr/><div class="validation-message visible">There have been some errors in creating your credentials';

            for(var i in errors) {
                errorHtml += "<p>" + errors[i] + ".</p>";
            }
            return errorHtml + "</div>";
        }

         var request = {
            "name": $('#name').val(),
            "email": $('#email').val(),
            "organisation": $('#organisation').val()
        };

        $.ajax({
             url : "/locate/create-user",
             type: "POST",
             dataType : 'json',
             contentType: "application/json",
             data : JSON.stringify(request),
             timeout : 10000
           }).
           done(function (data, status, xhrObj) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#results").html(printSuccess(obj));
           }).
           fail(function (xhrObj, status, errorStr) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#results").html(printError(obj));
             });
     });

    function print(obj) {
            var html = "<pre>" + JSON.stringify(obj, null, 4) + "</pre>";
            return html;
        }

    $('#submit-address-search').on("click", function(e) {
         e.preventDefault();

        var request_headers = {}
        request_headers['Authorization'] = 'Bearer ' + $('#bearer_token').val();

        var queryString = "?postcode=" + $('#postcode').val().replace(" ","").toLowerCase();

        if($('#format').val()) {
            queryString += "&format=" + $('#format').val();
        }

        if($('#query').val()) {
            queryString += "&query=" + $('#query').val();
        }

        $("#example-query-title").show();
        $("#example-query").html('<div class="credential">https://locate-api.herokuapp.com/locate/addresses' + queryString + '</div>');

        $.ajax({
             url : "https://locate-api.herokuapp.com/locate/addresses" + queryString,
             type: "GET",
             headers : request_headers,
             xhrFields: {withCredentials: true},
             timeout : 10000
           }).
           success(function (data, status, xhrObj) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#explorer-results").html(print(obj));
           }).
           error(function (xhrObj, status, errorStr) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#explorer-results").html(print(obj));
             });
     });


    $('#submit-authority-search').on("click", function(e) {
         e.preventDefault();

        var request_headers = {}
        request_headers['Authorization'] = 'Bearer ' + $('#bearer_token').val();

        $.ajax({
             url : "https://locate-api.herokuapp.com/locate/authority?postcode=" + $('#postcode').val().replace(" ","").toLowerCase(),
             type: "GET",
             headers : request_headers,
             xhrFields: {withCredentials: true},
             timeout : 10000
           }).
           success(function (data, status, xhrObj) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#explorer-results").html(print(obj));
           }).
           error(function (xhrObj, status, errorStr) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#explorer-results").html(print(obj));
             });
     });


}.call(this));
