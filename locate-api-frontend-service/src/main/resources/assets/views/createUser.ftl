<#include "header.ftl">
<article>
        <p>Free postcode lookup service for the UK public sector.</p>

        <h2>What is the Locate API?</h2>

        <p>Locate is a free API service that does postcode lookups against the latest available data from the Ordinance
            Survey. It uses <a href="http://www.ordnancesurvey.co.uk/business-and-government/products/addressbase-premium.html">AddressBase data</a> and it is free to use for any public sector organisation in the UK.</p>

        <p>Read more about Locate and how to access the service in the <a href="https://github.com/alphagov/locate-api">technical
            documentation on GitHub.</a></p>

        <p>Test your credentials on the <a href="/locate/explorer">API explorer.</a>
        <hr/>

        <h2>Generate an API token</h2>

        <p>We need a few details in order to generate your very own API token.</p>
        <form action="/locate/create-user" method="POST">
        <fieldset>
            <label for="name">
                Name
            </label>

            <input id="name" name="name" value="" autocomplete="off" class="text first-name long" type="text">

            <label for="email">
                Email
            </label>

            <input id="email" name="email" value="" autocomplete="off" class="text email long" type="text">

            <label for="organisation">
                Organisation
            </label>

            <input id="organisation" name="organisation" value="" autocomplete="off" class="text first-name long"
                   type="text">
        </fieldset>
        <hr/>

        <h3>What type of addresses do you want to receive from the API?</h3>
        <fieldset>

            <label for="residential_data" class="selectable">
                <input id="residential_data" name="queryType" value="residential" class="radio validate"
                       data-validation-name="residential" data-validation-type="field" data-validation-rules="nonEmpty"
                       type="radio">
                Residential
            </label>

            <label for="commercial_data" class="selectable">
                <input id="commercial_data" name="queryType" value="commercial" class="radio validate"
                       data-validation-name="commercial" data-validation-type="field" data-validation-rules="nonEmpty"
                       type="radio">
                Commercial
            </label>

            <label for="residential-commercial_data" class="selectable">
                <input id="residential-commercial_data" name="queryType" value="residentialAndCommercial"
                       class="radio validate" data-validation-name="residential-commercial" data-validation-type="field"
                       data-validation-rules="nonEmpty" type="radio">
                Residential and commercial
            </label>

             <label for="electoral_data" class="selectable">
                            <input id="electoral_data" name="queryType" value="electoral"
                                   class="radio validate" data-validation-name="electoral" data-validation-type="field"
                                   data-validation-rules="nonEmpty" type="radio">
                            Electoral
                        </label>

            <label for="all_data" class="selectable">
                <input id="all_data" name="queryType" value="all" class="radio validate" data-validation-name="all"
                       data-validation-type="field" data-validation-rules="nonEmpty" type="radio">
                All
            </label>
        </fieldset>

        <hr/>

        <h3>Do you want the presentation data or the full data block?</h3>
        <fieldset>
            <label for="presentation" class="selectable">
                <input id="presentation" name="dataType" value="presentation" class="radio"
                       data-validation-name="presentation" data-validation-type="field" data-validation-rules="nonEmpty"
                       type="radio">
                Presentation
            </label>
            <label for="full" class="selectable">
                <input id="full" name="dataType" value="all" class="radio" data-validation-name="full"
                       data-validation-type="field" data-validation-rules="nonEmpty" type="radio">
                Full
            </label>
        </fieldset>  

        <p>Example JSON: <span id="full-example-button" class="button">full</span> and <span id="presentation-example-button" class="button">presentation</span></p>

        <div id="full-example">
            <pre>
                <code>
                {
                    "gssCode": "E09000032",
                    "country": "England",
                    "postcode": "sw112dr",
                    uprn": "100023586417",
                    "createdAt": "2014-07-03",
                    "presentation": {
                        "property": "Arches 11 And 12",
                        "street": "Latchmere Road",
                        "town": "London",
                        "area": "Wandsworth",
                        "postcode": "SW11 2DR"
                    },
                    "details": {
                        "usrn": "22902824",
                        "isResidential": false,
                        "isCommercial": true,
                        "isElectoral": false,
                        "isPostalAddress": true,
                        "classification": "CI03",
                        "state": "approved",
                        "organisation": "Mwr Motors",
                        "primaryClassification": "Commercial",
                        "secondaryClassification": "Industrial Application",
                        "file": "TQ2575.csv",
                        "blpuUpdatedAt": "2004-12-21",
                        "blpuCreatedAt": "2001-03-19"
                    },
                    "location": {
                        "lat": 51.46865944238342,
                        "long": -0.16305944948944998
                    },
                    "ordering": {
                        "paoText": "ARCHES 11 AND 12",
                        "street": "LATCHMERE ROAD"
                    }
                }
                </code>
            </pre>
            <span id="close-full-example">[ close ]</span>
        </div>

    <div id="presentation-example">
        <pre>
            <code>
            {
                "property": "Flat A",
                "street": "93 Latchmere Road",
                "locality": "Battersea",
                "town": "London",
                "area": "Wandsworth",
                "postcode": "SW11 2DR",
                "uprn": "10090499727",
                "gssCode": "E09000032"
            }
            </code>
        </pre>
        <span id="close-presentation-example">[ close ]</span>
    </div>


        <hr/>

        <h2>I understand that:</h2>

        <p>By applying for a Locate API token I am confirming I represent a UK public sector organisation.</p>

        <p>Usage of the Locate API is limited to 1000 requests per calendar day.</p>

        <div class="warning">Locate API is in 'alpha' stage so there are not guaranteed up-time or service levels.</div>

        <input id="submit" class="button next" value="Generate token" type="submit">

        </form>

        <div id="results" aria-live="polite"></div>

    </article>
<#include "footer.ftl">
