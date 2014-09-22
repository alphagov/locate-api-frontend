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

        <h2>I understand that:</h2>

        <p>By applying for a Locate API token I am confirming I represent a UK public sector organisation.</p>

        <p>Usage of the Locate API is limited to 1000 requests per calendar day.</p>

        <div class="warning">Locate API is in 'alpha' stage so there are not guaranteed up-time or service levels.</div>

        <input id="submit" class="button next" value="Generate token" type="submit">

        </form>

        <div id="results" aria-live="polite"></div>

    </article>
<#include "footer.ftl">
